package interfaces.rest.politician;

import domain.politician.Politician;
import interfaces.rest.ViewMapper;

public class PoliticianViewMapper implements ViewMapper<Politician, PoliticianView> {

	@Override
	public PoliticianView entityToView(Politician politician) {
		PoliticianView view = new PoliticianView();
		
		// TODO
		
		return view;
	}
}
