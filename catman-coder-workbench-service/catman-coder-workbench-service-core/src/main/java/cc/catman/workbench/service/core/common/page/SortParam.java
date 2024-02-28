package cc.catman.workbench.service.core.common.page;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SortParam {
    @Getter
    final private List<Order> orders=new ArrayList<>();

    public static SortParam empty(){
        return new SortParam();
    }

    public SortParam addOrder(String property,String direction){
        Direction dir=Direction.of(direction);
        if(dir==null){
            log.warn("direction {} is not support",direction);
            dir=Direction.ASC;
            return this;
        }
        return addOrder(property,dir);
    }
    public SortParam addOrder(String property,Direction direction){
        orders.add(new Order(property,direction));
        return this;
    }

    public static class Order{
        private String property;
        private Direction direction;

        public Order(String property, Direction direction) {
            this.property = property;
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    public static enum Direction{
        ASC("ASC"),DESC("DESC");
        private String value;

        Direction(String value) {
            this.value = value;
        }

        public static Direction of(String value){
            if(StringUtils.hasText(value)){
                for (Direction direction : values()) {
                    if (direction.getValue().equalsIgnoreCase(value)){
                        return direction;
                    }
                }
            }
            return null;
        }
        public String getValue() {
            return value;
        }
    }
}
