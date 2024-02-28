package cc.catman.workbench.common;

import cc.catman.workbench.service.core.common.page.SortParam;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class VSortHelper {

    public static Sort with(SortParam sortParam){
        return with(sortParam,new ArrayList<>(0));
    }

    public static Sort with(SortParam sortParam, List<String> ignoreProperties){
        if (sortParam == null){
            return null;
        }
        List<SortParam.Order> orders = sortParam.getOrders().stream().filter(order -> !ignoreProperties.contains(order.getProperty())).toList();
        if (orders.isEmpty()){
            return null;
        }
        return Sort.by(orders
                .stream()
                .map(order -> new Sort.Order(Sort.Direction.fromString(order.getDirection().getValue()),order.getProperty()))
                .toList());
    }

    public static Sort with(SortParam sortParam, Sort sort){
        if (sort==null){
            return with(sortParam);
        }
        List<String> ignoreProperties = new ArrayList<>(0);
        sort.stream().map(Sort.Order::getProperty).forEach(ignoreProperties::add);
        Sort s= with(sortParam,ignoreProperties);
        return s.and(sort);
    }

    public static Sort with(SortParam sortParam, Sort sort, boolean replace){
       if(!replace){
           return with(sortParam,sort);
       }
        Sort sortPrior = with(sortParam);
        if (sortPrior==null){
            return sort;
        }
        if (sort==null){
            return sortPrior;
        }
        // 合并排序,优先使用sortParam中的排序
        List<String> existProperties = sortParam.getOrders().stream().map(SortParam.Order::getProperty).toList();
        sort.stream()
               .filter(order -> !existProperties.contains(order.getProperty()))
                .forEach(sort::and);
         return sortPrior;
    }
}
