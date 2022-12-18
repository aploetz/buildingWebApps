package com.aaron.buildingWebApps;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 2577395507474870921L;
	
	private TextField firstName = new TextField("First name"); 
	private TextField lastName = new TextField("Last name"); 
	private EmailField email = new EmailField("Email");
	private Binder<Person> binder = new Binder<>(Person.class);
	private Grid<Person> grid = new Grid<>(Person.class);
	
	PersonController personController;
	
	public MainView(PersonRepository personRepo) {
//		var button = new Button("Click me!");
//		var textField = new TextField();
//
//		add(new H1("Hello world!"));
//		add(new HorizontalLayout(textField, button));
//		
//		button.addClickListener(e -> {
//			add(new Paragraph("Hello, " + textField.getValue()));
//			textField.clear();
//		});
				
		personController  = new PersonController(personRepo);
		grid.setColumns("firstName","lastName","email");
		
		add(getForm(), grid);
		refreshGrid();
	}
	
	private Component getForm() {
		var layout = new HorizontalLayout();
		layout.setAlignItems(Alignment.BASELINE);
		
		var addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		layout.add(firstName, lastName, email, addButton);
		
		binder.bindInstanceFields(this);
		
		addButton.addClickListener(click -> {
			try {
				var person = new Person();
				binder.writeBean(person);
				personController.savePerson(person);
				clearFields();
				refreshGrid();
			} catch (ValidationException e) {
				
			}
		});
		
		return layout;
	}
	
	private void refreshGrid() {
		//  WARNING!!!  This app was adjusted to work with Spring.Data.Cassandra
		//  A repo.findAll() in Apache Cassandra will do a full table scan, which
		//  in a distributed DB like Cassandra, is a BAD thing, and shouldn't be
		//  done in production!
		//
		//  That being said, findAll is fine for the purposes of building a
		//  simple app connecting to Cassandra running on your localhost.
		
		grid.setItems(personController.getPersons());
	}
	
	private void clearFields() {
		firstName.clear();
		lastName.clear();
		email.clear();
	}
}
