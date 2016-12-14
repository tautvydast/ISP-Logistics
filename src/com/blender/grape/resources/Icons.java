package com.blender.grape.resources;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tautvydas Ramanauskas IF-4/12
 */
public interface Icons {
    ImageIcon MENU_BUTTON = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/menu_button.png")));
    ImageIcon ADD_USER = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/add_user.png")));
    ImageIcon EDIT_USER = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/edit_user.png")));
    ImageIcon REMOVE_USER = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/remove_user.png")));
    ImageIcon EDIT_USER_PERMISSIONS = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/edit_user_permissions.png")));
    ImageIcon ADD_ROLE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/add_role.png")));
    ImageIcon EDIT_ROLE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/edit_role.png")));
    ImageIcon REMOVE_ROLE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/remove_role.png")));

    ImageIcon DROP_HERE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/drop_here.png")));
    ImageIcon SELECT_FOLDER = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/icon-folder.png")));
    ImageIcon ADD = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/add.png")));
    ImageIcon REFRESH = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/refresh.png")));
    ImageIcon SAVE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/save.png")));
    ImageIcon LOAD = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/load.png")));
    ImageIcon REMOVE = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/remove.png")));
    ImageIcon TV = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Icons.class.getClassLoader().getResource("com/blender/grape/resources/icons/tv.png")));
}