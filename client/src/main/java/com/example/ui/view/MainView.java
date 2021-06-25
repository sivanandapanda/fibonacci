package com.example.ui.view;

import com.example.ui.service.FibService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@PWA(name = "Fibonacci App",
        shortName = "Fib App",
        description = "This is an Fibonacci application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView(@Autowired FibService service) {
        setWidth("100%");
        addClassName("centered-content");

        TextField textField = new TextField("Enter your index");
        textField.addThemeName("bordered");

        Button button = new Button("Submit",
                e -> {
                    Notification.show(service.calculateFib(textField.getValue()));
                    UI.getCurrent().getPage().reload();
                });

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        HorizontalLayout form = new HorizontalLayout(textField, button);
        form.addClassName("form");
        form.setWidth("100%");
        form.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        var indicesSeen = String.join(", ", service.getAll());
        H3 indexesSeenH3 = new H3("Indexes I have seen: " + indicesSeen);

        H3 calcValuesH3 = new H3("Calculated Values:");
        Div div = new Div();
        div.setWidthFull();
        service.getCurrent()
                .forEach(value -> {
                    Paragraph paragraph = new Paragraph("For index " + value.getIndex() + " I calculated " + value.getValue());
                    paragraph.setWidthFull();
                    div.add(paragraph);
                });

        add(form, indexesSeenH3, calcValuesH3, div);
    }

}
