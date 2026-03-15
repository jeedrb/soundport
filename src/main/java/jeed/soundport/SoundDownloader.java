package jeed.soundport;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public class SoundDownloader {
    // get the repos for the sound hashes and files
    private static final String MANIFEST = "https://piston-meta.mojang.com/v1/packages/d7aae43ea69d80cc3441bee4179abd791f6534cd/1.9.json";
    private static final String RESOURCE = "https://resources.download.minecraft.net";

    public static void soundDownload(final File soundsFolder) {
        Thread downloadThread = new Thread(new Runnable() { // so the game "doesn't stutter" or wha eva
            @Override
            public void run() {
                try {
                    // call the other methods :)
                    writePackMeta(soundsFolder);
                    writeSoundsJson(soundsFolder);

                    // make that folder if it isn't there
                    File assets = new File(soundsFolder, "assets/soundport/sounds");
                    if (!assets.exists())
                        assets.mkdirs();

                    // yummy data mmm
                    InputStreamReader reader = new InputStreamReader(new URL(MANIFEST).openStream());
                    JsonObject objects = new JsonParser().parse(reader).getAsJsonObject().getAsJsonObject("objects");

                    String[] witchTargets = { // list of witch sounds to yoink
                        "minecraft/sounds/entity/witch/ambient1.ogg",
                        "minecraft/sounds/entity/witch/ambient2.ogg",
                        "minecraft/sounds/entity/witch/ambient3.ogg",
                        "minecraft/sounds/entity/witch/ambient4.ogg",
                        "minecraft/sounds/entity/witch/ambient5.ogg",
                        "minecraft/sounds/entity/witch/death1.ogg",
                        "minecraft/sounds/entity/witch/death2.ogg",
                        "minecraft/sounds/entity/witch/death3.ogg",
                        "minecraft/sounds/entity/witch/hurt1.ogg",
                        "minecraft/sounds/entity/witch/hurt2.ogg",
                        "minecraft/sounds/entity/witch/hurt3.ogg",
                        "minecraft/sounds/entity/witch/drink1.ogg",
                        "minecraft/sounds/entity/witch/drink2.ogg",
                        "minecraft/sounds/entity/witch/drink3.ogg",
                        "minecraft/sounds/entity/witch/drink4.ogg",
                        "minecraft/sounds/entity/witch/throw1.ogg",
                        "minecraft/sounds/entity/witch/throw2.ogg",
                        "minecraft/sounds/entity/witch/throw3.ogg",
                    };

                    String[] blockTargets = {
                        // wood
                        "minecraft/sounds/dig/wood1.ogg",
                        "minecraft/sounds/dig/wood2.ogg",
                        "minecraft/sounds/dig/wood3.ogg",
                        "minecraft/sounds/dig/wood4.ogg",

                        // wool
                        "minecraft/sounds/dig/cloth1.ogg",
                        "minecraft/sounds/dig/cloth2.ogg",
                        "minecraft/sounds/dig/cloth3.ogg",
                        "minecraft/sounds/dig/cloth4.ogg",

                        // green
                        "minecraft/sounds/dig/grass1.ogg",
                        "minecraft/sounds/dig/grass2.ogg",
                        "minecraft/sounds/dig/grass3.ogg",
                        "minecraft/sounds/dig/grass4.ogg",


                    };

                    for (String path : witchTargets) {
                        if (objects.has(path)) {
                            String hash = objects.getAsJsonObject(path).get("hash").getAsString(); // get that hash broski
                            String remoteFile = path.substring(path.lastIndexOf('/') + 1);

                            File localFile = new File(assets, "witch_" + remoteFile); // putting witch_ before the sound names

                            if (!localFile.exists()) { // downloading time!!!!!
                                String remoteUrl = RESOURCE +  '/' + hash.substring(0, 2) + '/' + hash;
                                FileUtils.copyURLToFile(new URL(remoteUrl), localFile); // the Yoink:tm:
                                System.out.println("[SoundPort]: Downloaded sound \" " + remoteFile + " \" from Mojang.");
                            }
                        }
                    }

                    for (String path : blockTargets) {
                        if(objects.has(path)) {
                            String hash = objects.getAsJsonObject(path).get("hash").getAsString();
                            String remoteFile = path.substring(path.lastIndexOf('/') + 1);

//                            File localFile = new File(assets, "wood_" + remoteFile); // oops
                            File localFile = new File(assets, remoteFile);

                            if(!localFile.exists()) {
                                String remoteUrl = RESOURCE + '/' + hash.substring(0, 2) + '/' + hash;
                                FileUtils.copyURLToFile(new URL(remoteUrl), localFile);
                                System.out.println("[SoundPort]: Downloaded sound \" " + remoteFile + " \" from Mojang.");
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        downloadThread.start(); // this might be important
    }

    public static void writeSoundsJson(File rootFolder) {
        File jsonFile = new File(rootFolder, "assets/soundport/sounds.json");

        if (jsonFile.exists()) return;

        try { // be annoying and make the sounds.json
            StringBuilder steve = new StringBuilder(); // steve cause he builder
            steve.append("{\n");

            // witch sounds
            steve.append("  \"witch.idle\": { \"category\": \"hostile\", \"sounds\": [\"soundport:witch_ambient1\", \"soundport:witch_ambient2\", \"soundport:witch_ambient3\", \"soundport:witch_ambient4\", \"soundport:witch_ambient5\"] },\n");
            steve.append("  \"witch.death\": { \"category\": \"hostile\", \"sounds\": [\"soundport:witch_death1\", \"soundport:witch_death2\", \"soundport:witch_death3\"] },\n");
            steve.append("  \"witch.drink\": { \"category\": \"hostile\", \"sounds\": [\"soundport:witch_drink1\", \"soundport:witch_drink2\", \"soundport:witch_drink3\", \"soundport:witch_drink4\"] },\n");
            steve.append("  \"witch.hurt\": { \"category\": \"hostile\", \"sounds\": [\"soundport:witch_hurt1\", \"soundport:witch_hurt2\", \"soundport:witch_hurt3\"] },\n");
            steve.append("  \"witch.throw\": { \"category\": \"hostile\", \"sounds\": [\"soundport:witch_throw1\", \"soundport:witch_throw2\", \"soundport:witch_throw3\"] },\n");

            // wood sounds
            steve.append("  \"wood.place\": { \"category\": \"block\", \"sounds\": [\"soundport:wood1\", \"soundport:wood2\", \"soundport:wood3\", \"soundport:wood4\"] },\n");

            // wool sounds
            steve.append("  \"wool.place\": { \"category\": \"block\", \"sounds\": [\"soundport:cloth1\", \"soundport:cloth2\", \"soundport:cloth3\", \"soundport:cloth4\"] },\n");

            // green sounds
            steve.append("  \"green.place\": { \"category\": \"block\", \"sounds\": [\"soundport:grass1\", \"soundport:grass2\", \"soundport:grass3\", \"soundport:grass4\"] }\n");

            steve.append("}");

            FileUtils.writeStringToFile(jsonFile, steve.toString());
            System.out.println("[SoundPort]: Generated sounds.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writePackMeta(File rootFolder) {
        File metaFile = new File(rootFolder, "pack.mcmeta");
        if (metaFile.exists()) return;

        try { // be annoying and make the pack.mcmeta
            String content = "{\"pack\":{\"pack_format\":1,\"description\":\"by Jeed :)\"}}";
            FileUtils.writeStringToFile(metaFile, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
