package com.bootdo.modular.data.dao;


import com.bootdo.modular.data.domain.ProductDO;

import java.util.List;
import java.util.Map;

/**
 * 商品信息表
 *
 * @author yogiCai
 * @date 2017-11-18 22:41:14
 */
public interface ProductDao {

    ProductDO get(Integer id);

    List<ProductDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ProductDO product);

    int update(ProductDO product);

    int updateM(Map<String, Object> map);

    int remove(Integer id);

    int batchRemove(Integer[] ids);
}
