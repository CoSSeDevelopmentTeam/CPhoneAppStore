package net.comorevi.cosse.cphoneappstore;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import itsu.mcbe.form.base.CustomForm;
import itsu.mcbe.form.base.ModalForm;
import itsu.mcbe.form.base.SimpleForm;
import itsu.mcbe.form.element.Button;
import itsu.mcbe.form.element.Input;
import net.comorevi.cosse.cossephonecore.application.ApplicationBase;
import net.comorevi.cosse.cossephonecore.application.ApplicationDescription;
import net.comorevi.cosse.cossephonecore.application.ApplicationManager;
import net.comorevi.cosse.cossephonecore.phone.CoSSePhone;

import java.util.List;

public class FormProcessor {

    public static SimpleForm getAppListForm(CoSSePhone phone, Player player) {
        SimpleForm form = new SimpleForm()
            .setId(FormIDs.ID_APPLIST)
            .setTitle("CPhoneAppStore");

        int count = 0;

        for (ApplicationBase app : ApplicationManager.getApplications().values()) {
            String text = app.getApplicationDescription().getName() + "\n";
            text += ApplicationManager.getPlayerApplications(player.getName()).contains(app.getApplicationDescription().getName())
                    ? TextFormat.GREEN + "インストール済み"
                    : TextFormat.RESET + "未インストール";
            text += TextFormat.RESET + " / ";
            text += (app.getApplicationDescription().isDefault() ? TextFormat.BLUE + "誰でも可能" : TextFormat.YELLOW + "OP");

            form.addButton(new Button(text, "url", app.getApplicationDescription().getIconURL()) {
                @Override
                public void onClick(Player player) {
                    phone.sendForm(player, getAppDetailsForm(phone, player, this.getText().split("\n")[0]));
                }
            });

            count++;
        }

        form.setContent(TextFormat.AQUA + "インストール可能なアプリケーション (" + count + ")");

        return form;
    }

    public static CustomForm getSearchForm(CoSSePhone phone, Player player) {
        CustomForm form = new CustomForm() {
            @Override
            public void onEnter(Player player, List<Object> response) {
                phone.sendForm(player, getSearchResultForm(phone, player, String.valueOf(response.get(0))));
            }
        }
            .setId(FormIDs.ID_SEARCH)
            .setTitle("CPhoneAppStore - 検索")
            .addFormElement(new Input("", "検索"));

        return form;
    }

    public static SimpleForm getSearchResultForm(CoSSePhone phone, Player player, String search) {
        SimpleForm form = new SimpleForm()
            .setId(FormIDs.ID_SEARCH_RESULT)
            .setTitle("CPhoneAppStore - 検索結果");

        int count = 0;

        for (ApplicationBase app : ApplicationManager.getApplications().values()) {
            if (app.getApplicationDescription().getName().contains(search)) {
                String text = app.getApplicationDescription().getName() + "\n";
                text += ApplicationManager.getPlayerApplications(player.getName()).contains(app.getApplicationDescription().getName())
                        ? TextFormat.ITALIC + "" + TextFormat.GREEN + "インストール済み"
                        : TextFormat.ITALIC + "" + TextFormat.RESET + "未インストール";
                text += TextFormat.RESET + " / ";
                text += (app.getApplicationDescription().isDefault() ? TextFormat.BLUE + "誰でも可能" : TextFormat.YELLOW + "OP");

                form.addButton(new Button(text, "url", app.getApplicationDescription().getIconURL()) {
                    @Override
                    public void onClick(Player player) {
                        phone.sendForm(player, getAppDetailsForm(phone, player, this.getText().split("\n")[0]));
                    }
                });
                count++;
            }
        }

        form.setContent(TextFormat.AQUA + "検索結果 (" + count + ") - " + TextFormat.GRAY + "" + TextFormat.ITALIC + search);

        return form;
    }

    public static ModalForm getAppDetailsForm(CoSSePhone phone, Player player, String appName) {
        ModalForm form = new ModalForm() {
            @Override
            public void onButton1Click(Player player) {
                // TODO 購入処理
            }
        }
            .setId(FormIDs.ID_APP_DETAILS)
            .setTitle("CPhoneAppStore - " + appName);

        ApplicationDescription des = ApplicationManager.getApplicationByName(appName).getApplicationDescription();
        StringBuffer buf = new StringBuffer();

        buf.append(TextFormat.GREEN + "アプリ名: " + TextFormat.RESET + des.getName() + "\n");
        buf.append(TextFormat.GREEN + "バージョン: " + TextFormat.RESET + des.getVersion() + "\n");
        buf.append(TextFormat.GREEN + "デベロッパ名: " + TextFormat.RESET + des.getAuthor() + "\n");
        buf.append(TextFormat.GREEN + "インストール権限: " + TextFormat.RESET + des.getPermission() + "\n");
        buf.append(TextFormat.GREEN + "価格: " + TextFormat.RESET + des.getPrice() + "\n");
        buf.append("\n");
        buf.append(
            ApplicationManager.getPlayerApplications(player.getName()).contains(des.getName())
                ? TextFormat.ITALIC + "" + TextFormat.AQUA + "インストール済み\n"
                : TextFormat.ITALIC + "" + TextFormat.GRAY + "未インストール\n"
        );
        buf.append("\n");
        buf.append(TextFormat.GREEN + "説明: \n");
        buf.append(TextFormat.RESET + des.getDescription());

        if (ApplicationManager.getPlayerApplications(player.getName()).contains(des.getName())) form.setButton1Text("Close");
        else form.setButton1Text(TextFormat.GREEN + "インストール");

        form.setContent(buf.toString());
        form.setButton2Text("閉じる");

        return form;
    }

    public static SimpleForm getBoughtAppsForm(CoSSePhone phone, Player player) {
        SimpleForm form = new SimpleForm()
                .setId(FormIDs.ID_BOUGHT)
                .setTitle("CPhoneAppStore - 購入済み");

        int count = 0;

        for (String appName : ApplicationManager.getPlayerApplications(player.getName())) {
            ApplicationBase app = ApplicationManager.getApplicationByName(appName);

            String text = app.getApplicationDescription().getName() + "\n";
            text += ApplicationManager.getPlayerApplications(player.getName()).contains(app.getApplicationDescription().getName())
                    ? TextFormat.GREEN + "インストール済み"
                    : TextFormat.RESET + "未インストール";
            text += TextFormat.RESET + " / ";
            text += (app.getApplicationDescription().isDefault() ? TextFormat.BLUE + "誰でも可能" : TextFormat.YELLOW + "OP");

            form.addButton(new Button(text, "url", app.getApplicationDescription().getIconURL()) {
                @Override
                public void onClick(Player player) {
                    phone.sendForm(player, getAppDetailsForm(phone, player, this.getText().split("\n")[0]));
                }
            });

            count++;
        }

        form.setContent(TextFormat.AQUA + "購入済みアプリケーション一覧 (" + count + ")");

        return form;
    }

    public static SimpleForm getRecommendedForm(CoSSePhone phone, Player player, List<String> recommended) {
        SimpleForm form = new SimpleForm()
                .setId(FormIDs.ID_RECOMMENDED)
                .setTitle("CPhoneAppStore - 購入済み");

        int count = 0;

        for (String appName : recommended) {
            ApplicationBase app = ApplicationManager.getApplicationByName(appName);

            String text = app.getApplicationDescription().getName() + "\n";
            text += ApplicationManager.getPlayerApplications(player.getName()).contains(app.getApplicationDescription().getName())
                    ? TextFormat.GREEN + "インストール済み"
                    : TextFormat.RESET + "未インストール";
            text += TextFormat.RESET + " / ";
            text += (app.getApplicationDescription().isDefault() ? TextFormat.BLUE + "誰でも可能" : TextFormat.YELLOW + "OP");

            form.addButton(new Button(text, "url", app.getApplicationDescription().getIconURL()) {
                @Override
                public void onClick(Player player) {
                    phone.sendForm(player, getAppDetailsForm(phone, player, this.getText().split("\n")[0]));
                }
            });

            count++;
        }

        form.setContent(TextFormat.AQUA + "おすすめアプリケーション (" + count + ")");

        return form;
    }
}
