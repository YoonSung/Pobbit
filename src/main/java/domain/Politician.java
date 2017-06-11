package domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "politicians")
public class Politician {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private List<Integer> numberOfElection;
	
	private String district;
	
	private String imageUrl;
	
	//TODO enum
	private String partyName;
	
	private String committeeName;
	
	private String description;
}
