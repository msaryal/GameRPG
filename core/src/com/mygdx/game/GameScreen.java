package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.characters.Enemy;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Hero;

import java.util.*;

public class GameScreen {
    private SpriteBatch batch;
    private Stage stage;
    private BitmapFont font24;
    private Map map;
    private ItemsEmitter itemsEmitter;
    private TextEmitter textEmitter;
    private Hero hero;
    private boolean paused;
    private float spawnTimer;

    private List<GameCharacter> allCharacters;
    private List<Enemy> allEnemies;
    private Comparator<GameCharacter> drawOrderComparator;

    public Map getMap() {
        return map;
    }

    public List<Enemy> getAllEnemies() {
        return allEnemies;
    }

    public Hero getHero() {
        return hero;
    }

    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    public GameScreen(SpriteBatch batch){
        this.batch = batch;
    }

    public void create(){
        map = new Map();
        allCharacters = new ArrayList<>();
        allEnemies = new ArrayList<>();
        hero = new Hero(this);
        itemsEmitter = new ItemsEmitter();
        textEmitter = new TextEmitter();
        allCharacters.addAll(Arrays.asList(
                hero,
                new Enemy(this),
                new Enemy(this),
                new Enemy(this),
                new Enemy(this),
                new Enemy(this),
                new Enemy(this)
        ));
        for (int i = 0; i < allCharacters.size(); i++) {
            if(allCharacters.get(i) instanceof Enemy){
                allEnemies.add(((Enemy) allCharacters.get(i)));
            }
        }
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        stage = new Stage();

        Skin skin = new Skin();
        skin.add("simpleButton", new Texture("SimpleButton.png"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        Group menuGroup = new Group();
        menuGroup.addActor(pauseButton);
        menuGroup.addActor(exitButton);
        exitButton.setPosition(150, 0);
        menuGroup.setPosition(740, 600);
        stage.addActor(menuGroup);
        Gdx.input.setInputProcessor(stage);

        drawOrderComparator = new Comparator<GameCharacter>(){
            @Override
            public int compare(GameCharacter o1, GameCharacter o2) {
                return (int) (o2.getPosition().y - o1.getPosition().y);
            }
        };
    }

    public void render(){
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        map.render(batch);
        Collections.sort(allCharacters, drawOrderComparator);
        for(int i = 0; i < allCharacters.size(); i++){
            allCharacters.get(i).render(batch, font24);
        }
        itemsEmitter.render(batch);
        textEmitter.render(batch, font24);
        hero.renderHUD(batch, font24);
        batch.end();
        stage.draw();
    }

    public void update(float dt){
        if (!paused) {
            spawnTimer += dt;
            if (spawnTimer > 3.0f) {
                Enemy enemy = new Enemy(this);
                allCharacters.add(enemy);
                allEnemies.add(enemy);
                spawnTimer = 0.0f;
            }
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).update(dt);
            }

            for (int i = 0; i < allEnemies.size(); i++) {
                Enemy currentEnemy = allEnemies.get(i);
                if (!currentEnemy.isAlive()) {
                    allEnemies.remove(currentEnemy);
                    allCharacters.remove(currentEnemy);
                    itemsEmitter.generateRandomItem(currentEnemy.getPosition().x, currentEnemy.getPosition().y, 5, 0.6f);
                    hero.killEnemy(currentEnemy);
                }
            }
            for (int i = 0; i < itemsEmitter.getItems().length; i++) {
                Item it = itemsEmitter.getItems()[i];
                if (it.isActive()) {
                    float dst = hero.getPosition().dst(it.getPosition());
                    if (dst < 24.0f) {
                        hero.useItem(it);
                    }
                }
            }
            itemsEmitter.update(dt);
            textEmitter.update(dt);
        }
        stage.act(dt);
    }
}
