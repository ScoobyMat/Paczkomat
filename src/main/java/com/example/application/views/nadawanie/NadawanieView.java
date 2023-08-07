package com.example.application.views.nadawanie;

import com.example.application.data.entity.Paczka;
import com.example.application.data.entity.Skrytka;
import com.example.application.data.service.PaczkaService;
import com.example.application.data.service.SkrytkaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Random;

@PageTitle("Nadawanie")
@Route(value = "Nadawanie", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class NadawanieView extends Composite<VerticalLayout> {
    private JavaMailSender javaMailSender;
    private Paragraph textLarge = new Paragraph();

    private HorizontalLayout layoutRow = new HorizontalLayout();

    private TextField textField = new TextField();

    private EmailField emailField = new EmailField();

    private ComboBox<String> rozmiarComboBox = new ComboBox<>();

    private ComboBox<String> typComboBox = new ComboBox<>();

    private Button PrzyciskNadaj = new Button();

    private SkrytkaService skrytkaService;
    private PaczkaService paczkaService;


    public NadawanieView(SkrytkaService skrytkaService, PaczkaService paczkaService,JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.skrytkaService = skrytkaService;
        this.paczkaService = paczkaService;
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
        rozmiarComboBox.setLabel("Rozmiar paczki");
        rozmiarComboBox.setItems("S", "L", "XL");
        rozmiarComboBox.setHeightFull();
        typComboBox.setLabel("Typ przesyłki");
        typComboBox.setItems("Długoterminowa", "Krótkoterminowa");
        typComboBox.setHeightFull();
        PrzyciskNadaj.setText("Nadaj");
        PrzyciskNadaj.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        PrzyciskNadaj.addClickListener(e -> NadajClick());
        getContent().add(textLarge);
        getContent().add(layoutRow);
        layoutRow.add(textField);
        layoutRow.add(emailField);
        layoutRow.add(rozmiarComboBox);
        layoutRow.add(typComboBox);
        getContent().add(PrzyciskNadaj);
    }

    private void NadajClick() {
        String rozmiar = rozmiarComboBox.getValue();
        String nadawca = textField.getValue();
        String odbiorcaEmail = emailField.getValue();

        Paczka paczka = new Paczka();
        paczka.setNadawca(nadawca);
        paczka.setOdbiorcaEmail(odbiorcaEmail);
        paczka.setRozmiarPaczki(rozmiar);

        List<Skrytka> wolneSkrytki = skrytkaService.getWolneSkrytki(rozmiar);

        if (!wolneSkrytki.isEmpty()) {
            Random random = new Random();
            Skrytka selectedSkrytka = wolneSkrytki.get(random.nextInt(wolneSkrytki.size()));
            paczka.setSkrytka(selectedSkrytka);
            selectedSkrytka.setPaczka(paczka);
            selectedSkrytka.setStatus(Skrytka.StatusSkrytki.Zajęta);
            paczkaService.update(paczka);
            skrytkaService.update(selectedSkrytka);

            wyslijKodOdbioruEmail(odbiorcaEmail, paczka.getKodOdbioru(), paczka.getId());
            Notification.show("Paczka została nadana, wiadomość z kodem odbioru została wysłana!", 5000, Notification.Position.MIDDLE);
        } else {
            Notification notification = new Notification("Brak wolnych skrytek", 3000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }

        textField.clear();
        emailField.clear();
        rozmiarComboBox.clear();
        typComboBox.clear();
    }

    public void wyslijKodOdbioruEmail(String odbiorcaEmail, String kodOdbioru, Long paczkaId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(odbiorcaEmail);
        message.setSubject("Kod odbioru paczki - Numer przesyłki" + paczkaId);
        message.setText("Twój kod odbioru paczki: " + paczkaId + "to:" + kodOdbioru);

        try {
            javaMailSender.send(message);
            Notification.show("Wiadomość z kodem odbioru została wysłana!", 3000, Notification.Position.MIDDLE);
        } catch (MailException e) {
            Notification.show("Błąd podczas wysyłania wiadomości e-mail.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            e.printStackTrace();
        }
    }


    public class KodOdbioruGenerator {

        private static final int DLUGOSC_KODU = 6;
        private static final String ZNAKI = "0123456789";
        private static final Random random = new Random();

        public static String generujKodOdbioru() {
            StringBuilder pwd = new StringBuilder(DLUGOSC_KODU);
            for (int i = 0; i < DLUGOSC_KODU; i++) {
                int index = random.nextInt(ZNAKI.length());
                pwd.append(ZNAKI.charAt(index));
            }
            return pwd.toString();
        }
    }
}
