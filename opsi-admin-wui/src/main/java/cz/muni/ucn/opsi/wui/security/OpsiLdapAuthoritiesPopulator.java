/**
 *
 */
package cz.muni.ucn.opsi.wui.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.ldap.core.ContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.util.StringUtils;

/**
 * @author dosoudilj
 *
 */
public class OpsiLdapAuthoritiesPopulator extends
		DefaultLdapAuthoritiesPopulator {

	private String userGroup = null;
	private String userRole = "ROLE_USER";
	private String adminGroup = null;
	private String adminRole = "ROLE_ADMIN";
	private boolean convertToUpperCase = true;
    private String rolePrefix = "ROLE_";

	/**
	 * @param contextSource
	 * @param groupSearchBase
	 */
	public OpsiLdapAuthoritiesPopulator(ContextSource contextSource,
			String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator#getGroupMembershipRoles(java.lang.String, java.lang.String)
	 */
	@Override
	public Set<GrantedAuthority> getGroupMembershipRoles(String userDn,
			String username) {
		Set<GrantedAuthority> authorities = super.getGroupMembershipRoles(userDn, username);
		Set<String> roles = new HashSet<String>();
		for (GrantedAuthority authority : authorities) {
			roles.add(authority.getAuthority());
		}

		addGroupRoles(authorities, roles, adminGroup, adminRole);
		addGroupRoles(authorities, roles, userGroup, userRole);

		return authorities;
	}

	private void addGroupRoles(Set<GrantedAuthority> authorities,
			Set<String> roles, String group, String role) {
		if (StringUtils.hasText(group)) {
			String roleName = rolePrefix;
			if (convertToUpperCase) {
				roleName += group.toUpperCase();
			} else {
				roleName += group;
			}
			if (roles.contains(roleName)) {
				authorities.add(new GrantedAuthorityImpl(role));
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator#setConvertToUpperCase(boolean)
	 */
	@Override
	public void setConvertToUpperCase(boolean convertToUpperCase) {
		this.convertToUpperCase = convertToUpperCase;
		super.setConvertToUpperCase(convertToUpperCase);
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator#setRolePrefix(java.lang.String)
	 */
	@Override
	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
		super.setRolePrefix(rolePrefix);
	}

	/**
	 * @param adminGroup the adminGroup to set
	 */
	public void setAdminGroup(String adminGroup) {
		this.adminGroup = adminGroup;
	}
	/**
	 * @return the adminGroup
	 */
	public String getAdminGroup() {
		return adminGroup;
	}

	/**
	 * @param userGroup the userGroup to set
	 */
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	/**
	 * @return the userGroup
	 */
	public String getUserGroup() {
		return userGroup;
	}

	/**
	 * @param adminRole the adminRole to set
	 */
	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}
	/**
	 * @return the adminRole
	 */
	public String getAdminRole() {
		return adminRole;
	}
	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}
}
