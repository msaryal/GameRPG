package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
// Код написан с помощью вебинара GeekBrains.
public class MyGdxGame extends ApplicationAdapter {
	/*
	=== Идеи ===
	+ 1. Движение по пискелям.
	+ 2. Преграды.
	+ 3. Анимация.
	4. Выстрелы снарядов.
	+ 5. Хаотичное движение для монстра.
	+ 6. Преследование монстром героя.
	+ 7. Аптечки, Монеты, Зелья (что можно поднять).
	+- 8. Параметры героям/монстрам (ХП, крит. шанс, скорость).
	+ 9. Система уровней игры.
	+ 10. Опыт героя.
	+ 11. Оружие.
	12. Финальный босс.
	+ 13. Драка с монстрами.
	+ 14. Полоска ХП.
	+ 15. Перенос на вектора.
	16. Инвентарь.
	+ 17. Отображение текста.
	18. Хот бар.
	-Камера-
	 */
	private SpriteBatch batch;
	private GameScreen gameScreen;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.gameScreen = new GameScreen(batch);
		this.gameScreen.create();
	}

	@Override
	public void render () {
		gameScreen.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}