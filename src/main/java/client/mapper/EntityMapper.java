package client.mapper;

public interface EntityMapper<View, Entity> {
	Entity create(View view);
}
