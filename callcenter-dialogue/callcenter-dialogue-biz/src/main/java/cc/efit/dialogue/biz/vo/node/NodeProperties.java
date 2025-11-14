package cc.efit.dialogue.biz.vo.node;

import java.util.List;

/**
 * @author across
 * @Description
 * @Date 2025-08-10 10:01
 */

public record NodeProperties(String title, String content, List<NodeIntention> intentions) {
}
