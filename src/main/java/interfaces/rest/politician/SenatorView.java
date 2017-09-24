package interfaces.rest.politician;

import lombok.Data;

@Data
public class SenatorView {
	private double attendance;
	private double election;
	private double billProposal;
	private double lawAbiding;
	private double property;
	private double contribution;
}
