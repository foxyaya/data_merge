package ybx66.aop;


import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ybx66.anno.Decrypt;
import ybx66.anno.Encrypt;
import ybx66.configure.MyRequestWrapper;
import ybx66.utils.AesUtil;


import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/30 16:06
 * @description aop 拦截请求，对数据传输进行加解密
 */
@Aspect
@Component
public class SafetyAspect {
    //slat随机，可以采用RSA对slat进行加密 ，请求体带着 这时候肯定要给前端发送一份 所以也需要对salt进行加密（RSA） 就固定啦
    //固定salt 后面可以对用户进行16长度salt设置
    private final static String key="r0NSnXLgGD2GSIb3";
    /**
     * Pointcut 切入点
     * 匹配ybx66.controller包下面的所有方法
     */
    @Pointcut(value = "execution(public * ybx66.controller.*.*(..))")
    public void safetyAspect() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "safetyAspect()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            //request对象
            HttpServletRequest request = attributes.getRequest();
            //http请求方法  post get
            String httpMethod = request.getMethod().toLowerCase();

            //method方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();

            //method方法上面的注解
            Annotation[] annotations = method.getAnnotations();

            //方法的形参参数
            Object[] args = pjp.getArgs();

            //是否有@Decrypt
            boolean hasDecrypt = false;
            //是否有@Encrypt
            boolean hasEncrypt = false;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Decrypt.class) {
                    hasDecrypt = true;
                }
                if (annotation.annotationType() == Encrypt.class) {
                    hasEncrypt = true;
                }
            }

            //前端公钥
//            String publicKey = null;

            //jackson
            ObjectMapper mapper = new ObjectMapper();
            //jackson 序列化和反序列化 date处理
            mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            //执行方法之前解密，且只拦截post请求
            if ("post".equals(httpMethod) && hasDecrypt) {
                String data = null;
                // 遇到的问题：由于前端采用的是Content-Type: application/json;charset=UTF-8 类型的数据格式
                //后台使用request.getParameter("xx") 获取到的是为空的数据，因为从request源码上来看，采用Content-Type: application/json;charset=UTF-8格式
                //的数据它不会进行参数的设置，所以获取到的值为空；问题来了：request在cotroller和aop中获取body都需要对request进行流式读取，但request不支持二次读取
                //这是由于它的源码所限制的，它的内部有个指针，指向数组的位置，当一次读取完毕之后 到了option=-1位置，后续的读写都会发生异常
                //思索过程: 第一次的想法是看request是否有clone()方法 对其进行深拷贝，但很遗憾的是，request不能被序列化和拷贝，想法破碎
                // 那么就考虑能不能对其备份，多次使用：
                //解决问题：采用MyRequestWrapper类 对HttpServletRequestWrapper进行修饰，也是修饰者模式，当然需要请求经过滤器之手（重点）
                //MyRequestWrapper对request请求数据进行数据备份，实现了request的多次读取使用。
                MyRequestWrapper wrapper =new MyRequestWrapper(request);
                StringBuffer sb = new StringBuffer();
                String res = "";
                BufferedReader reader = wrapper.getReader();
                while ((res = reader.readLine())!=null){
                    sb.append(res);
                }
                data =sb.toString();


                //AES解密得到明文data数据
                JSONObject parse = JSONObject.parseObject(data);
                data =parse.getString("data");
                String decrypt = AesUtil.decrypt(data, key);
                System.out.println("解密出来的data数据：" + decrypt);

                //设置到方法的形参中，目前只能设置只有一个参数的情况
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                if(args.length > 0){
                    args[0] = mapper.readValue(decrypt, args[0].getClass());
                }
            }

            //执行并替换最新形参参数   PS：这里有一个需要注意的地方，method方法必须是要public修饰的才能设置值，private的设置不了（原因在于切点的设置）
            Object o = pjp.proceed(args);

            //返回结果之前加密
            String data =null;
            if (hasEncrypt) {
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //每次响应之前随机获取AES的key，加密data数据
                System.out.println("AES的key：" + key);
                String dataString = mapper.writeValueAsString(o);
                System.out.println("需要加密的data数据：" + dataString);
                 data = AesUtil.encrypt(dataString, key);

                //用前端的公钥来解密AES的key，并转成Base64
//                String aesKey = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(key.getBytes(), publicKey));

                //转json字符串并转成Object对象，设置到Result中并赋值给返回值o
                ResultMessageDTO resultMessageDTO =(ResultMessageDTO)o;
                System.out.println("resultMessageDTO = " + resultMessageDTO);
                //json手动给vo类赋值
                o = (mapper.readValue("{\"code\":\"" + resultMessageDTO.getCode() + "\",\"desc\":\"" + resultMessageDTO.getDesc() + "\",\"message\":\"" + data + "\"}", ResultMessageDTO.class));
            }

            //返回
//            System.out.println("o = " + o);
            return o;

        } catch (Throwable e) {
            System.err.println(pjp.getSignature());
            e.printStackTrace();
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"加解密异常");
        }
    }
}
