package org.maxkey.persistence.service;


import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.maxkey.constants.ConstantsStatus;
import org.maxkey.crypto.ReciprocalUtils;
import org.maxkey.crypto.password.PasswordReciprocal;
import org.maxkey.domain.ChangePassword;
import org.maxkey.domain.UserInfo;
import org.maxkey.identity.kafka.KafkaIdentityAction;
import org.maxkey.identity.kafka.KafkaIdentityTopic;
import org.maxkey.identity.kafka.KafkaProvisioningService;
import org.maxkey.persistence.mapper.UserInfoMapper;
import org.maxkey.util.DateUtils;
import org.maxkey.util.StringUtils;
import org.maxkey.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * @author Crystal.Sea
 *
 */
@Service
public class UserInfoService extends JpaBaseService<UserInfo> {
	final static Logger _logger = LoggerFactory.getLogger(UserInfoService.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	KafkaProvisioningService kafkaProvisioningService;
	
	public UserInfoService() {
		super(UserInfoMapper.class);
	}

	/* (non-Javadoc)
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public UserInfoMapper getMapper() {
		// TODO Auto-generated method stub
		return (UserInfoMapper)super.getMapper();
	}
	
    public boolean insert(UserInfo userInfo) {
        userInfo = passwordEncoder(userInfo);
        if (super.insert(userInfo)) {
            kafkaProvisioningService.send(
                    KafkaIdentityTopic.USERINFO_TOPIC, userInfo, KafkaIdentityAction.CREATE_ACTION);
            return true;
        }

        return false;
    }
	
	public boolean update(UserInfo userInfo) {
		 if(super.update(userInfo)){
		     kafkaProvisioningService.send(
		             KafkaIdentityTopic.USERINFO_TOPIC, userInfo, KafkaIdentityAction.UPDATE_ACTION);
			 return true;
		 }
		 return false;
	}
	
	public boolean delete(UserInfo userInfo) {
		if( super.delete(userInfo)){
		    kafkaProvisioningService.send(
		            KafkaIdentityTopic.USERINFO_TOPIC, userInfo, KafkaIdentityAction.DELETE_ACTION);
			 return true;
		}
		return false;
	}

	public boolean updateProtectedApps(UserInfo userinfo) {
		try {
			if(WebContext.getUserInfo() != null) {
				userinfo.setModifiedBy(WebContext.getUserInfo().getId());
			}
			userinfo.setModifiedDate(DateUtils.getCurrentDateTimeAsString());
			return getMapper().updateProtectedApps(userinfo) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public UserInfo loadByUsername(String username) {
		return getMapper().loadByUsername(username);
	}
	
	public UserInfo loadByAppIdAndUsername(String appId,String username){
		try {
			UserInfo userinfo = new UserInfo();
			userinfo.setUsername(username);
			return getMapper().loadByAppIdAndUsername(userinfo) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public void logisticDeleteAllByCid(String cid){
		try {
			 getMapper().logisticDeleteAllByCid(cid);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public UserInfo passwordEncoder(UserInfo userInfo) {
	    String password = passwordEncoder.encode(PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(), userInfo.getPassword()));
        userInfo.setDecipherable(ReciprocalUtils.encode(PasswordReciprocal.getInstance().rawPassword(userInfo.getUsername(), userInfo.getPassword())));
        _logger.debug("decipherable : "+userInfo.getDecipherable());
        userInfo.setPassword(password);
        userInfo.setPasswordLastSetTime(DateUtils.getCurrentDateTimeAsString());
        
        userInfo.setModifiedDate(DateUtils.getCurrentDateTimeAsString());
        
        return userInfo;
	}
	public boolean changePassword(UserInfo userInfo) {
		try {
			if(WebContext.getUserInfo() != null) {
				userInfo.setModifiedBy(WebContext.getUserInfo().getId());
				
			}
			userInfo = passwordEncoder(userInfo);
			
			if(getMapper().changePassword(userInfo) > 0){
				ChangePassword changePassword=new ChangePassword();
				changePassword.setId(userInfo.getId());
				changePassword.setUid(userInfo.getId());
				changePassword.setUsername(userInfo.getUsername());
				changePassword.setDecipherable(userInfo.getDecipherable());
				changePassword.setPassword(userInfo.getPassword());
				kafkaProvisioningService.send(
				        KafkaIdentityTopic.PASSWORD_TOPIC, changePassword, KafkaIdentityAction.PASSWORD_ACTION);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean changeAppLoginPassword(UserInfo userinfo) {
		try {
			if(WebContext.getUserInfo() != null) {
				userinfo.setModifiedBy(WebContext.getUserInfo().getId());
			}
			userinfo.setModifiedDate(DateUtils.getCurrentDateTimeAsString());
			return getMapper().changeAppLoginPassword(userinfo) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 锁定用户：islock：1 用户解锁 2 用户锁定
	 * @param userInfo
	 */
	public void locked(UserInfo userInfo) {
		try {
			if(userInfo != null && StringUtils.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstantsStatus.STOP);
				getMapper().locked(userInfo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户登录成功后，重置错误密码次数和解锁用户
	 * @param userInfo
	 */
	public void unlock(UserInfo userInfo) {
		try {
			if(userInfo != null && StringUtils.isNotEmpty(userInfo.getId())) {
				userInfo.setIsLocked(ConstantsStatus.START);
				userInfo.setBadPasswordCount(0);
				getMapper().unlock(userInfo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新错误密码次数
	 * @param userInfo
	 */
	public void updateBadPasswordCount(UserInfo userInfo) {
		try {
			if(userInfo != null && StringUtils.isNotEmpty(userInfo.getId())) {
				int updateBadPWDCount = userInfo.getBadPasswordCount() + 1;
				userInfo.setBadPasswordCount(updateBadPWDCount);
				getMapper().updateBadPWDCount(userInfo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean changeSharedSecret(UserInfo userInfo){
		return getMapper().changeSharedSecret(userInfo)>0;
	}
	
	public boolean changePasswordQuestion(UserInfo userInfo){
		return getMapper().changePasswordQuestion(userInfo)>0;
	}
	
	public boolean changeAuthnType(UserInfo userInfo){
		return getMapper().changeAuthnType(userInfo)>0;
	}
	
	public boolean changeEmail(UserInfo userInfo){
		return getMapper().changeEmail(userInfo)>0;
	}
	
	public boolean changeMobile(UserInfo userInfo){
		return getMapper().changeMobile(userInfo)>0;
	}
	
    public UserInfo queryUserInfoByEmailMobile(String emailMobile) {
        return getMapper().queryUserInfoByEmailMobile(emailMobile);
    }
    
    public int updateProfile(UserInfo userInfo){
        
        return getMapper().updateProfile(userInfo);
    }

}
