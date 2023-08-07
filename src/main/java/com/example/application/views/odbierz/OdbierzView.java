package com.example.application.views.odbierz;

import com.example.application.data.entity.Paczka;
import com.example.application.data.entity.Skrytka;
import com.example.application.data.service.PaczkaService;
import com.example.application.data.service.SkrytkaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Odbierz")
@Route(value = "Odbierz", layout = MainLayout.class)
@Uses(Icon.class)
public class OdbierzView extends Composite<VerticalLayout> {

    private PaczkaService paczkaService;
    private SkrytkaService skrytkaService;
    private HorizontalLayout layoutRow = new HorizontalLayout();
    private TextField textField = new TextField();
    private TextField textField2 = new TextField();
    private Button buttonPrimary = new Button();

    public OdbierzView(PaczkaService paczkaService, SkrytkaService skrytkaService) {
        this.paczkaService = paczkaService;
        this.skrytkaService = skrytkaService;
        getContent().setHeightFull();
        getContent().setWidthFull();
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        textField.setLabel("Numer przesyłki");
        textField2.setLabel("Kod odbioru");
        buttonPrimary.setText("Odbierz");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(e -> onButtonPrimaryClick());
        getContent().add(layoutRow);
        layoutRow.add(textField);
        layoutRow.add(textField2);
        getContent().add(buttonPrimary);
    }

    private void onButtonPrimaryClick() {
        String numerPrzesylki = textField.getValue();
        String kodOdbioru = textField2.getValue();

        try {
            long idPaczki = Long.parseLong(numerPrzesylki);
            Paczka paczka = paczkaService.findPaczkaById(idPaczki);

            if (paczka != null && paczka.getKodOdbioru().equals(kodOdbioru)) {
                Skrytka skrytka = paczka.getSkrytka();

                if (skrytka != null) {
                    paczka.setStatus(Paczka.StatusPaczki.Odebrana);
                    paczka.setSkrytka(skrytka);
                    paczkaService.update(paczka);

                    skrytka.setStatus(Skrytka.StatusSkrytki.Wolna);
                    skrytkaService.update(skrytka);

                    Notification notification = new Notification("Paczka została odebrana", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();
                }
            } else {
                Notification notification = new Notification("Nieudana próba odbioru paczki - nieprawidłowy numer przesyłki lub kod odbioru", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        } catch (NumberFormatException e) {
            Notification notification = new Notification("Niepoprawny numer przesyłki", 3000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }
}
