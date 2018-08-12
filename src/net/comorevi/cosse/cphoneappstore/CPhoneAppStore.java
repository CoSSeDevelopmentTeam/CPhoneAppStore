package net.comorevi.cosse.cphoneappstore;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import itsu.mcbe.form.base.SimpleForm;
import itsu.mcbe.form.element.Button;
import net.comorevi.cosse.cossephonecore.application.ApplicationBase;
import net.comorevi.cosse.cossephonecore.phone.CoSSePhone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CPhoneAppStore extends ApplicationBase {

    private List<String> recommended;

    @Override
    public void onEnable() {
        new File("./plugins/CoSSePhoneCore/AppStore/").mkdirs();

        if (!new File("./plugins/CoSSePhoneCore/AppStore/recommended.yml").exists()) {
            try {
                Files.copy(this.getClass().getClassLoader().getResourceAsStream("recommended.yml"), new File("./plugins/CoSSePhoneCore/AppStore/recommended.yml").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Config config = new Config(new File("./plugins/CoSSePhoneCore/AppStore/recommended.yml"), Config.YAML);
        config.load("./plugins/CoSSePhoneCore/AppStore/recommended.yml");
        recommended = new ArrayList<>(config.getList("Recommended"));
    }

    @Override
    public void startApplication(CoSSePhone phone, Player player) {
        SimpleForm menu = new SimpleForm() {
            @Override
            public void onEnter(Player player, int index) {
                switch (index) {
                    case 0: {
                        phone.sendForm(player, FormProcessor.getAppListForm(phone, player));
                        break;
                    }

                    case 1: {
                        phone.sendForm(player, FormProcessor.getRecommendedForm(phone, player, recommended));
                        break;
                    }

                    case 2: {
                        break;
                    }

                    case 3: {
                        phone.sendForm(player, FormProcessor.getSearchForm(phone, player));
                        break;
                    }

                    case 4: {
                        phone.sendForm(player, FormProcessor.getBoughtAppsForm(phone, player));
                        break;
                    }
                }
            }
        }

        .setContent("")
        .setId(FormIDs.ID_MENU)
        .setTitle("CPhoneAppStore");

        menu.addButton(new Button(TextFormat.AQUA + "ストア", "url", "https://raw.githubusercontent.com/CoSSeDevelopmentTeam/CPhoneAppStore/master/icons/store.png"));
        menu.addButton(new Button("おすすめ", "url", "https://raw.githubusercontent.com/CoSSeDevelopmentTeam/CPhoneAppStore/master/icons/recommended.png"));
        menu.addButton(new Button("ランキング", "url", "https://raw.githubusercontent.com/CoSSeDevelopmentTeam/CPhoneAppStore/master/icons/ranking.png"));
        menu.addButton(new Button("検索", "url", "https://raw.githubusercontent.com/CoSSeDevelopmentTeam/CPhoneAppStore/master/icons/search.png"));
        menu.addButton(new Button("購入済み", "url", "https://raw.githubusercontent.com/CoSSeDevelopmentTeam/CPhoneAppStore/master/icons/bought.png"));

        phone.sendForm(player, menu);
    }

}
