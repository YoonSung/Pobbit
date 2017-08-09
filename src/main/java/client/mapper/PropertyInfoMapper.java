package client.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import client.opencongress.OpenCongressClient.PoliticianDetailPage;
import domain.politician.DeclaredProperties;
import domain.politician.PropertyInfo;

@Component
public class PropertyInfoMapper implements EntityMapper<PoliticianDetailPage, PropertyInfo> {
	@Override
	public PropertyInfo create(PoliticianDetailPage view) {
		PropertyInfo entity = new PropertyInfo();
		Optional.ofNullable(view.getDeclaredPropertyHistory()).ifPresent(list -> entity.setDeclaredProperties(new DeclaredProperties(list)));
		entity.setPoliticalContribution(view.getPoliticalContribution());
		return entity;
	}
}
