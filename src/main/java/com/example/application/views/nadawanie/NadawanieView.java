package com.example.application.views.nadawanie;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Nadawanie")
@Route(value = "Nadawanie", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class NadawanieView extends Composite<VerticalLayout> {

    private Paragraph textLarge = new Paragraph();

    private HorizontalLayout layoutRow = new HorizontalLayout();

    private TextField textField = new TextField();

    private EmailField emailField = new EmailField();

    private MultiSelectComboBox multiSelectComboBox = new MultiSelectComboBox();

    private MultiSelectComboBox multiSelectComboBox2 = new MultiSelectComboBox();

    private Button buttonPrimary = new Button();

    private Grid multiSelectGrid = new Grid(SamplePerson.class);

    private Button buttonPrimary2 = new Button();

    public NadawanieView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
        textLarge.setText("Nadaj paczkę");
        textLarge.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        textField.setLabel("Imię nadawcy");
        textField.setHeightFull();
        emailField.setLabel("Email odbiorcy");
        emailField.setHeightFull();
        multiSelectComboBox.setLabel("Rozmiar paczki");
        multiSelectComboBox.setHeightFull();
        setMultiSelectComboBoxSampleData(multiSelectComboBox);
        multiSelectComboBox2.setLabel("Typ przesyłki");
        multiSelectComboBox2.setHeightFull();
        setMultiSelectComboBoxSampleData(multiSelectComboBox2);
        buttonPrimary.setText("Szukaj skrytki");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        multiSelectGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        setGridSampleData(multiSelectGrid);
        buttonPrimary2.setText("Nadaj");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(textLarge);
        getContent().add(layoutRow);
        layoutRow.add(textField);
        layoutRow.add(emailField);
        layoutRow.add(multiSelectComboBox);
        layoutRow.add(multiSelectComboBox2);
        getContent().add(buttonPrimary);
        getContent().add(multiSelectGrid);
        getContent().add(buttonPrimary2);
    }

    private void setMultiSelectComboBoxSampleData(MultiSelectComboBox multiSelectComboBox) {
        multiSelectComboBox.setItems("First", "Second", "Third", "Fourth");
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    @Autowired()
    private SamplePersonService samplePersonService;
}
