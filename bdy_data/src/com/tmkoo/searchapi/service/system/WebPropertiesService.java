package com.tmkoo.searchapi.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.tmkoo.searchapi.common.Global;
import com.tmkoo.searchapi.entity.WebProperties;
import com.tmkoo.searchapi.repository.WebPropertiesDao;

@Component// Spring Bean的标识
@Transactional// 类中所有public函数都纳入事务管理的标识.
public class WebPropertiesService {

	private WebPropertiesDao webPropertiesDao;

	public WebProperties getWebProperties(Long id) {
		return webPropertiesDao.findOne(id);
	}

	public void saveWebProperties(WebProperties entity) {
		webPropertiesDao.save(entity);
	}

	public void deleteWebProperties(Long id) {
		webPropertiesDao.delete(id);
	}

	public List<WebProperties> getAllWebProperties() {
		return (List<WebProperties>) webPropertiesDao.findAll();
	}

	public Page<WebProperties> getAllWebProperties( Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<WebProperties> spec = buildSpecification(searchParams);

		return webPropertiesDao.findAll(spec, pageRequest);
	}

	 

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<WebProperties> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<WebProperties> spec = DynamicSpecifications.bySearchFilter(filters.values(), WebProperties.class);
		return spec;
	} 

	@Autowired
	public void setWebPropertiesDao(WebPropertiesDao webPropertiesDao) {
		this.webPropertiesDao = webPropertiesDao;
	}

	public void fillGlobal() {
		WebProperties webProperties = getAllWebProperties().get(0);
		Global.webProperties = new com.tmkoo.searchapi.vo.WebProperties();
		Global.webProperties.WEB_NAME = webProperties.getWebName();
		Global.webProperties.CSS_TEMPLATE_NAME=webProperties.getCssTemeplateName();
		Global.webProperties.REGISTER_OPEN=Boolean.parseBoolean(webProperties.getRegisterOpen());
		Global.webProperties.DES_KEY=webProperties.getDesKey();
		Global.webProperties.MAX_SEARCH_COUNT_UNLOGIN=webProperties.getMaxSearchCountUnlogin();
		Global.webProperties.MAX_SEARCH_COUNT_LOGIN=webProperties.getMaxSearchCountLogin();
		Global.webProperties.MAX_INFO_COUNT_UNLOGIN=webProperties.getMaxInfoCountUnlogin();
		Global.webProperties.MAX_INFO_COUNT_LOGIN=webProperties.getMaxInfoCountLogin();
		Global.webProperties.API_KEY=webProperties.getApiKey();
		Global.webProperties.API_PASSWORD=webProperties.getApiPassword();
	}
}
