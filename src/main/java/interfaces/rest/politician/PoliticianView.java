package interfaces.rest.politician;

import java.util.List;

import lombok.Data;

@Data
public class PoliticianView {
	// basic info
	private String name;
	private String party;
	private String contact;
	private String email;
	private String imageUrl;
	private String academyHistory;
	private String careers;
	private List<String> committee;
	
	// election info
	private List<Integer> electionTurns;
	private String district;
	private double totalVotePercent;
	private double electionVotePercent;
	private boolean isRepresentation;
	
	// activity
	private double plenaryMettingAttendanceRate;
	private double averageCommitteeAttendanceRate;
	private int billProposalCount;
	
	// property
	private long property;
	private long contribution;
}
