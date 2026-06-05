package net.tiktokadmin.autovault.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AutoVaultCommand {
    // Настройки мода по умолчанию
    public static Identifier targetConfiguredBlock = new Identifier("minecraft", "ominous_vault");
    public static Identifier targetConfiguredItem = new Identifier("minecraft", "heavy_core");
    public static boolean modEnabled = false;

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("autovault")
                // 1. Выбор блока с автодополнением из игры
                .then(ClientCommandManager.argument("block", IdentifierArgumentType.identifier())
                    .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.BLOCK.getIds(), builder))
                    
                    // 2. Выбор предмета с автодополнением из игры
                    .then(ClientCommandManager.argument("item", IdentifierArgumentType.identifier())
                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                        
                        // 3. Включение/выключение (true/false)
                        .then(ClientCommandManager.argument("active", BoolArgumentType.bool())
                            .executes(context -> {
                                targetConfiguredBlock = IdentifierArgumentType.getIdentifier(context, "block");
                                targetConfiguredItem = IdentifierArgumentType.getIdentifier(context, "item");
                                modEnabled = BoolArgumentType.getBool(context, "active");

                                String statusColor = modEnabled ? "§aВКЛ" : "§cВЫКЛ";
                                context.getSource().sendFeedback(Text.literal(
                                    "§6[AutoVault] §7Настройки сохранены!\n" +
                                    "§7Блок: §e" + targetConfiguredBlock + "\n" +
                                    "§7Предмет: §e" + targetConfiguredItem + "\n" +
                                    "§7Статус: " + statusColor
                                ));
                                return 1;
                            })))));
        });
    }
}
