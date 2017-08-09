package interfaces.rest;

public interface ViewMapper<Entity, View> {
	View entityToView(Entity entity);
}
