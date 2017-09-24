package interfaces.rest.politician;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PoliticianView {
	private Integer id;

	// basic info
	private String name;
	private String party;
	private String contact;
	private String email;
	private String imageUrl;
	private String academyHistory;
	private String careers;
	private List<String> committee = new ArrayList<>();

	// election info
	private List<Integer> electionTurns = new ArrayList<>();
	private String district;
	private double totalVotePercent;
	private double electionVotePercent;
	private boolean isRepresentation;
	
	// activity
	private double plenaryMeetingAttendanceRate;
	private double averageCommitteeAttendanceRate;
	private int billProposalCount;

	// property
	private long property;
	private long contribution;

	// criminal record info
	private int criminalRecordCount;

	// senator info
	private SenatorView senator;

	boolean isGreaterAttendanceRateThan(PoliticianView other) {
		return this.getAverageAttendanceRate() > other.getAverageAttendanceRate();
	}

	double getAverageAttendanceRate() {
		return (
				this.plenaryMeetingAttendanceRate + 
				this.averageCommitteeAttendanceRate
		       ) / 2;
	}

	boolean isElectedMoreThan(PoliticianView other) {
		return this.electionTurns.size() 
		       > other.electionTurns.size();
	}

	int getElectionCount() {
		return electionTurns.size();
	}
	
	boolean isBillProposalMoreThan(PoliticianView other) {
		return this.billProposalCount > other.billProposalCount;
	}

	boolean hasMoreCriminalRecordCountThan(PoliticianView other) {
		return this.criminalRecordCount > other.criminalRecordCount;
	}

	boolean isRicherThan(PoliticianView other) {
		return this.property > other.property;
	}

	boolean isMoreContributedThan(PoliticianView other) {
		return this.contribution > other.contribution;
	}
}
