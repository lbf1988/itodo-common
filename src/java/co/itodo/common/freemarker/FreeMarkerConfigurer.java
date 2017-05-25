package co.itodo.common.freemarker;

import co.itodo.common.freemarker.shiro.ShiroTags;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public class FreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable(ShiroTags.TAGS_NAME, new ShiroTags());
    }
}
