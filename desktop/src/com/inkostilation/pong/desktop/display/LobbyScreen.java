package com.inkostilation.pong.desktop.display;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.inkostilation.pong.commands.request.RequestConnectionCommand;
import com.inkostilation.pong.commands.request.RequestGameListCommand;
import com.inkostilation.pong.commands.request.RequestNewGameCommand;
import com.inkostilation.pong.desktop.controls.InputSystem;
import com.inkostilation.pong.desktop.network.Network;
import com.inkostilation.pong.engine.PongGame;
import com.inkostilation.pong.exceptions.NoEngineException;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.util.adapter.ListAdapter;
import com.kotcrab.vis.ui.util.adapter.SimpleListAdapter;
import com.kotcrab.vis.ui.widget.*;
import com.inkostilation.pong.desktop.display.ObserverTree.LobbyNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LobbyScreen implements Screen {

    private Stage view;
    private InputMultiplexer multiplexer = new InputMultiplexer();
    private ArrayList<String[]> list = new ArrayList<>();

    @Override
    public void show() {
        if (!VisUI.isLoaded()) {
            VisUI.load(VisUI.SkinScale.X1);
        }

        view = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(view);


        list.add(new String[]{"test", "second", "123"});
        list.add(new String[]{"1111", "----", "\\./"});
        list.add(new String[]{"test", "second", "123"});

        ArrayListAdapter<String[], HorizontalGroup> test = new ArrayListAdapter<String[], HorizontalGroup>(list) {
            @Override
            protected HorizontalGroup createView(String[] item) {
                HorizontalGroup table = new HorizontalGroup();
                for (String i : item) {
                    VisLabel label = new VisLabel(i);
                    table.addActor(label);
                }
                return table;
            }
        };

        VisTable table = new VisTable();
        table.setFillParent(true);
        view.addActor(table);
        table.row().fill().expandY();

        ObserverTree<VisTable, PongGame> tree = new ObserverTree<VisTable, PongGame>(PongGame.class) {
            @Override
            VisTable toActor(PongGame data) {
                VisTable treeTable = new VisTable();
                treeTable.row().expandX().fillX();
                treeTable.add(new VisLabel(data.getGameState().name)).padRight(300);
                treeTable.add(new VisLabel(data.getPlayersNumber() + ""));
                return treeTable;
            }
        };

        ListView<String[]> view = new ListView<>(test);
        view.setItemClickListener(new ListView.ItemClickListener<String[]>() {
            @Override
            public void clicked(String[] item) {
                System.out.println(Arrays.toString(item));
            }
        });

        VerticalGroup group = new VerticalGroup();
        group.addActor(new VisLabel("test"));
        group.pack();
        view.setHeader(group);

        VisWindow listWindow = new VisWindow("Lobby");
        VisTable listTable = new VisTable();
        listTable.setFillParent(true);
        listTable.row().padTop(20);
        listTable.add(group);
        listTable.row().expandY().expandX().fillX().fill().align(Align.center);
        listTable.add(tree);
        listTable.pack();
        listTable.align(Align.top);

        listWindow.addActor(listTable);
        listWindow.setMovable(false);
        listWindow.getTitleTable().removeActor(listWindow.getTitleLabel());
        listWindow.getTitleTable().row();
        listWindow.getTitleTable().add(new VisLabel("Lobby")).center();
        listWindow.getTitleTable().pack();

        view.rebuildView();
        table.add(listWindow).padTop(10).expandY().fill();

        table.row();
        VisTable bottomTable = new VisTable();
        table.add(bottomTable);

        bottomTable.bottom().padRight(100).padTop(10).padBottom(10);

        VisTextButton refreshButton = new VisTextButton("Refresh");
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    Network.getEngine().sendCommand(new RequestGameListCommand());
                } catch (IOException | NoEngineException e) {
                    e.printStackTrace();
                }
            }
        });

        VisTextButton connectButton = new VisTextButton("Connect");
        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    if (tree.getSelection().getLastSelected() != null) {
                        int index = tree.getSelection().getLastSelected().getPos();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new PongScreen());
                        Network.getEngine().sendCommand(new RequestConnectionCommand(index));
                    }
                } catch (IOException | NoEngineException e) {
                    e.printStackTrace();
                }
            }
        });

        VisTextButton newButton = new VisTextButton("New");
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PongScreen());
                    Network.getEngine().sendCommand(new RequestNewGameCommand());
                } catch (IOException | NoEngineException e) {
                    e.printStackTrace();
                }
            }
        });

        bottomTable.add(refreshButton).padLeft(50);
        bottomTable.add(connectButton).padLeft(50);
        bottomTable.add(newButton).padLeft(50);
        //table.pack();

        try {
            Network.getEngine().sendCommand(new RequestGameListCommand());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoEngineException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        view.act();
        view.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
