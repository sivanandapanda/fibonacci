package com.example.ui;

import com.example.ui.model.Value;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route
@PWA(name = "Fibonacci App",
        shortName = "Fib App",
        description = "This is an Fibonacci application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView(@Autowired FibService service) {

        TextField textField = new TextField("Enter your index");
        textField.addThemeName("bordered");

        Button button = new Button("Submit",
                e -> Notification.show(service.calculateFib(textField.getValue())));

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);
        addClassName("centered-content");

        var indicesSeen = service.getAll().stream().map(Value::getIndex).map(String::valueOf).collect(Collectors.joining(","));
        H3 indexesSeenH3 = new H3("Indexes I have seen: " + indicesSeen);


        H3 calcValuesH3 = new H3("Calculated Values:");
        Div div = new Div();
        service.getAll()
                .forEach(value -> div.add(new Paragraph("For index " + value.getIndex() + " I calculated X")));

        add(textField, button, indexesSeenH3, calcValuesH3, div);
    }

}
