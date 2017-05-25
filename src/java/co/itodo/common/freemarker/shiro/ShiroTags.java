package co.itodo.common.freemarker.shiro;

import freemarker.template.SimpleHash;

/**
 * @Desc
 *  * Shortcut for injecting the tags into Freemarker
 * <p>Usage: cfg.setSharedVeriable("shiro", new ShiroTags());</p>
 * @Author by Brant
 * @Date 2016/11/10
 */
public class ShiroTags extends SimpleHash {
    public static final String TAGS_NAME = "shiro";
    public ShiroTags() {
        put("authenticated", new AuthenticatedTag());
        put("guest", new GuestTag());
        put("hasAnyRoles", new HasAnyRolesTag());
        put("hasPermission", new HasPermissionTag());
        put("hasRole", new HasRoleTag());
        put("lacksPermission", new LacksPermissionTag());
        put("lacksRole", new LacksRoleTag());
        put("notAuthenticated", new NotAuthenticatedTag());
        put("principal", new PrincipalTag());
        put("user", new UserTag());
    }
}