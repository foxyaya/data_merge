package cn.ybx66.item.controller;

import cn.ybx66.conmmon.pojo.PageResult;
import cn.ybx66.item.pojo.Brand;
import cn.ybx66.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/3 23:08
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> BrandByList(
            @RequestParam(value ="key",required = false) String key,
            @RequestParam(value ="page",defaultValue = "1") Integer page,
            @RequestParam(value ="rows",defaultValue = "5") Integer rows,
            @RequestParam(value ="desc",defaultValue = "false") Boolean desc,
            @RequestParam(value ="sortBy",required = false) String sortBy
    ){
        return ResponseEntity.ok(brandService.BrandByList(page,rows,sortBy,desc,key));
    }

    @PostMapping
    public ResponseEntity<Void> insertBrand( Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.insertBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
