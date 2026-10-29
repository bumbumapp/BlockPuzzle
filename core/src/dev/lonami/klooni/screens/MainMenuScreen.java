/*
    1010! Klooni, a free customizable puzzle game for Android and Desktop
    Copyright (C) 2017-2019  Lonami Exo @ lonami.dev

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.lonami.klooni.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import dev.lonami.klooni.AdsController;
import dev.lonami.klooni.Klooni;
import dev.lonami.klooni.actors.SoftButton;

// Main menu screen, presenting some options (play, customize…)
public class MainMenuScreen extends InputListener implements Screen {

    //region Members

    private final Klooni game;
    private final Stage stage;

    //endregion

    //region Static members

    // As the examples show on the LibGdx wiki
    private static final float minDelta = 1 / 30f;

    //endregion
    AdsController adsController;
    //region Constructor

    public MainMenuScreen(Klooni game, final AdsController adsController) {
        this.game = game;
        this.adsController=adsController;
        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Bento header: big title and the best scores, in the theme's text colour.
        final Label.LabelStyle titleStyle = new Label.LabelStyle(game.skin.getFont("font"), Klooni.theme.textColor);
        final Label.LabelStyle smallStyle = new Label.LabelStyle(game.skin.getFont("font_small"),
                new Color(Klooni.theme.textColor).mul(1f, 1f, 1f, 0.62f));
        final Label title = new Label("Block Puzzle", titleStyle);
        table.add(title).colspan(2).left().padBottom(8);
        table.row();
        final Label best = new Label("Best " + Klooni.getMaxScore() + "   ·   Time " + Klooni.getMaxTimeScore(), smallStyle);
        best.setAlignment(Align.left);
        best.setFontScale(0.8f);
        table.add(best).colspan(2).left().padBottom(40);
        table.row();

        // Play button
        final SoftButton playButton = new SoftButton(
                0, GameScreen.hasSavedData() ? "play_saved_texture" : "play_texture");
        playButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.this.game.transitionTo(
                        new GameScreen(MainMenuScreen.this.game, GameScreen.GAME_MODE_SCORE,adsController));
            }
        });
        table.add(playButton).colspan(2).fill().space(32);

        table.row();

        // Star button (on GitHub)



        // Time mode
        final SoftButton statsButton = new SoftButton(2, "stopwatch_texture");
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.this.game.transitionTo(
                        new GameScreen(MainMenuScreen.this.game, GameScreen.GAME_MODE_TIME,adsController));
            }
        });
        table.add(statsButton).space(32);

        // Palette button (buy colors)
        final SoftButton paletteButton = new SoftButton(3, "palette_texture");
        paletteButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                // Don't dispose because then it needs to take us to the previous screen
                MainMenuScreen.this.game.transitionTo(new CustomizeScreen(
                        MainMenuScreen.this.game, MainMenuScreen.this.game.getScreen(),adsController), false);
            }
        });
        table.add(paletteButton).space(32);

        // Labels under the two small tiles.
        table.row();
        final Label timeLabel = new Label("Time mode", smallStyle);
        final Label themesLabel = new Label("Themes", smallStyle);
        timeLabel.setFontScale(0.7f);
        themesLabel.setFontScale(0.7f);
        table.add(timeLabel).padTop(-16);
        table.add(themesLabel).padTop(-16);
    }

    //endregion

    //region Screen

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Klooni.theme.glClearBackground();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), minDelta));
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    //endregion

    //region Unused methods

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    //endregion
}
