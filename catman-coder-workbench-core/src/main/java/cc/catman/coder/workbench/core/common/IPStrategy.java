package cc.catman.coder.workbench.core.common;

import io.netty.handler.ipfilter.IpSubnetFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IP策略,用于控制IP的访问信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPStrategy {
    /**
     * 策略ID
     */
    private String id;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 是否禁止访问
     */
    private Boolean isDeny;

    public boolean canAccess(String ip){
        if(this.ip == null){
            return this.isDeny;
        }
        boolean match=IpHelper.isIPInSubnet(ip,this.ip);
        return this.isDeny != match;
    }

    public boolean isValidate(){
        if(this.ip == null){
            return false;
        }
        return IpHelper.isCIDR(this.ip)||IpHelper.isIPAddress(this.ip);
    }
}
