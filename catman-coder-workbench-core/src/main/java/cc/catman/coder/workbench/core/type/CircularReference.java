package cc.catman.coder.workbench.core.type;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CircularReference {
    /**
     * 循环引用的起始点,此处的起始点是相对的,距离根节点最近的那个节点是起始点
     */
    private String start;
    /**
     * 循环引用的结束点
     */
    private String end;

    /**
     * 循环引用的路径,从起始点到结束点的路径,需要注意他和endToStartPath的区别
     * 假设下面的引用定义: a->b->c->d->e->f->b
     * 那么startToEndPath为: a->b->c->d->e->f->b
     * endToStartPath为: b->c->d->e->f->b
     * 或者说这里其实是出现了自引用,其实根据startToEndPath可以直接得到endToStartPath,比如:
     * startToEndPath: a->b->c->d->e->f->b,那么endToStartPath: b->c->d->e->f->b,
     * 当然这里针对的是TypeDefinition是如此,但如果换成是job的话,那么就不一定了,比如:
     * a->b->c->d->e->f
     * if (f) -> b->c->d->e->f
     * else    ->g->h->i->j
     * 可以看到f后面是有两个引用的,一个是自引用,一个是g,所以必须声明start和end,否则无法判断
     * 同理start to end ,也有可能有多条路径,比如:
     * a->b->c->d->e->f
     * if (f) -> a
     * else    ->g->h->i->a
     */
//    private List<String> startToEndPath;
//    /**
//     * 循环引用的路径,从结束点到起始点的路径
//     */
//    private List<String> endToStartPath;
}
