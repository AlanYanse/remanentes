package rotp.ui.main;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;

public final class MainButtonPanel extends BasePanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private Color buttonBackground;
    private GradientPaint nextTurnBackground;
    private GradientPaint nextTurnDisableBackground;
    private GradientPaint nextTurnHoverBackground;
    private GradientPaint nextTurnDepressedBackground;
    
    // Colores para estados de los botones
    private final Color buttonNormalColor = new Color(123, 123, 123);    // Gris medio retro
    private final Color buttonHoverColor = new Color(0, 0, 255);     // Gris claro para hover
    private final Color buttonPressedColor = new Color(90, 90, 90);      // Gris oscuro para pressed
    
    private final Color buttonTextC = new Color(220, 220, 220);          // Gris claro para texto
    private final Color buttonTextHoverC = Color.WHITE;        // Blanco para hover de texto
    private final Color buttonTextPressedC = new Color(200, 200, 150);   // Amarillo apagado para pressed
    private final Color textShadowC = new Color(30, 30, 30, 150);        // Sombra más oscura
    
    private final Color nextTurnDisableLightC = new Color(128,128,128);
    private final Color nextTurnDisableDarkC = new Color(64,64,64);
    private final Color nextTurnLightC = new Color(143,174,76);
    private final Color nextTurnDarkC = new Color(26,56,0);
    private final Color nextTurnBorderC = new Color(231,231,231);
    private final Color nextTurnTextC = Color.white;
    

    private boolean allowNextTurn = true;
    int leftM;
    int midM = -1;
    int rightM = -1;
    int botM;
    int buttonW;
    String[] buttons = { "MAIN_NAVIGATION_GAME",
                    "MAIN_NAVIGATION_SYSTEMS",
                    "MAIN_NAVIGATION_FLEETS",
                    "MAIN_NAVIGATION_DESIGN",
                    "MAIN_NAVIGATION_RACES",
                    "MAIN_NAVIGATION_COLONIES",
                    "MAIN_NAVIGATION_TECH" };

    Rectangle[] buttonBox = new Rectangle[buttons.length];
    Rectangle nextTurnBox = new Rectangle();
    Shape hoverBox, depressedBox;

    private final MainUI parent;

    public MainButtonPanel(MainUI p) {
        parent = p;
        leftM = s1;
        botM = s2;

        for (int i=0;i<buttonBox.length;i++)
            buttonBox[i] = new Rectangle();

        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public int buttonW() { return buttonW; }
    
    private void initColors(int w) {  // Cambié el nombre del método
        midM = w - scaled(273);
        rightM = w - s2;

        // Color sólido para los botones (estado normal)
        buttonBackground = buttonNormalColor;
        
        // Gradientes para Next Turn (mantienes estos)
        nextTurnBackground = new GradientPaint(midM, 0, nextTurnLightC, rightM, 0, nextTurnDarkC);
        nextTurnDisableBackground = new GradientPaint(midM, 0, nextTurnDisableLightC, rightM, 0, nextTurnDisableDarkC);
        nextTurnHoverBackground = new GradientPaint(midM, 0, nextTurnLightC, rightM, 0, hoverC);
        nextTurnDepressedBackground = new GradientPaint(midM, 0, nextTurnLightC, rightM, 0, depressedC);        
    }
    
    @Override
    public String textureName() { return TEXTURE_GRAY; }
    
    public void init() {
        allowNextTurn = false;
        new Thread(slowEnableNextTurn()).start();
    }
    
    private Runnable slowEnableNextTurn() {
        return () -> {
            sleep(750);
            allowNextTurn = true;
            repaint();
        };
    }
    
    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        int w = getWidth();
        int h = getHeight();
        
        Graphics2D g = (Graphics2D) g0;
        
        if (buttonBackground == null) 
            initColors(w);  // Ahora llama a initColors
        
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);

        buttonW = (midM - leftM) / buttons.length;

        for (int i = 0; i < buttons.length; i++)
            drawButton(g, i, leftM + (i * buttonW), s2, buttonW - s2, h - s2 - botM);

        drawNextTurn(g, midM, s2, rightM - midM, h - s3 - botM);
    }
    
    private void drawButton(Graphics2D g, int i, int x, int y, int w, int h) {
        // Determina el color de fondo según el estado
        Color bgColor;
        Color textColor;
        
        if (depressedBox == buttonBox[i]) {
            bgColor = buttonPressedColor;
            textColor = buttonTextPressedC;
        } else if (hoverBox == buttonBox[i]) {
            bgColor = buttonHoverColor;
            textColor = buttonTextHoverC;
        } else {
            //bgColor = buttonBackground;
            bgColor = buttonNormalColor;  // Usa buttonNormalColor directamente
            textColor = buttonTextC;
        }

        // **Relleno DOBLE para cubrir la textura**
        g.setColor(bgColor);
        g.fillRect(x, y, w, h);
        g.fillRect(x, y, w, h);  // Pinta dos veces para mejor cobertura
        
        // Relleno con color sólido
        //g.setColor(bgColor);
        //g.fillRect(x, y, w, h);

        
        // Bordes biselados para efecto retro
        if (depressedBox == buttonBox[i]) {
            // Estado presionado - bordes invertidos (luz desde abajo-derecha)
            g.setColor(new Color(60, 60, 60));  // Gris oscuro
            g.drawLine(x, y, x + w - 1, y);      // Línea superior
            g.drawLine(x, y, x, y + h - 1);      // Línea izquierda
            
            g.setColor(new Color(180, 180, 180)); // Gris claro
            g.drawLine(x + w - 1, y, x + w - 1, y + h - 1); // Línea derecha
            g.drawLine(x, y + h - 1, x + w - 1, y + h - 1); // Línea inferior
        } else {
            // Estado normal - bordes estándar (luz desde arriba-izquierda)
            g.setColor(new Color(180, 180, 180)); // Gris claro
            g.drawLine(x, y, x + w - 1, y);      // Línea superior
            g.drawLine(x, y, x, y + h - 1);      // Línea izquierda
            
            g.setColor(new Color(60, 60, 60));   // Gris oscuro
            g.drawLine(x + w - 1, y, x + w - 1, y + h - 1); // Línea derecha
            g.drawLine(x, y + h - 1, x + w - 1, y + h - 1); // Línea inferior
        }

        buttonBox[i].setBounds(x, y, w, h);
        String label = text(buttons[i]);
        g.setFont(narrowFont(28));
        int sw = g.getFontMetrics().stringWidth(label);
        int x0 = x + ((w - sw) / 2);
        
        // Dibuja texto con sombra
        drawShadowedString(g, label, 2, x0, y + h - s18, textShadowC, textColor);
    }
    
    private void drawNextTurn(Graphics2D g, int x, int y, int w, int h) {
        if (!allowNextTurn)
            g.setPaint(nextTurnDisableBackground);
        else if (depressedBox == nextTurnBox)
            g.setPaint(nextTurnDepressedBackground);
        else if (hoverBox == nextTurnBox)
            g.setPaint(nextTurnHoverBackground);
        else
            g.setPaint(nextTurnBackground);
        //g.fillRoundRect(x, y, w, h, s10, s10);
        g.fillRect(x, y, w, h);
        nextTurnBox.setBounds(x, y, w, h);

        Stroke prevS = g.getStroke();
        g.setStroke(stroke2);
        g.setColor(nextTurnBorderC);
        //g.drawRoundRect(x, y, w, h, s10, s10);
        g.drawRect(x, y, w, h);
        g.setStroke(prevS);

        String label = UserPreferences.displayYear() ? 
                      text("MAIN_NAVIGATION_NEXT_YEAR") : 
                      text("MAIN_NAVIGATION_NEXT_TURN");
        g.setFont(narrowFont(28));
        int sw = g.getFontMetrics().stringWidth(label);
        int x0 = x + ((w - sw) / 2);
        g.setColor(buttonTextC);
        drawShadowedString(g, label, 3, x0, y + h - s18, textShadowC, nextTurnTextC);
    }
    
    private void clickButton(int i) {
        RotPUI.instance().mainUI().cancel();
        switch(i) {
            case 0: RotPUI.instance().selectGamePanel();    break;
            case 1: RotPUI.instance().selectSystemsPanel(); break;
            case 2: RotPUI.instance().selectFleetPanel();   break;
            case 3: RotPUI.instance().selectDesignPanel();  break;
            case 4: RotPUI.instance().selectRacesPanel();   break;
            case 5: RotPUI.instance().selectPlanetsPanel(); break;
            case 6: RotPUI.instance().selectTechPanel();    break;
            default: break;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) {
        if ((hoverBox != null) || (depressedBox != null)) {
            depressedBox = null;
            hoverBox = null;
            repaint();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (hoverBox != null) {
            depressedBox = hoverBox;
            repaint();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 3)
            return;
        int x = e.getX();
        int y = e.getY();

        depressedBox = null;

        if (!parent.enableButtons())
            return;

        if (session().performingTurn()) {
            misClick();
            return;
        }

        int click = 0;
        if (allowNextTurn && nextTurnBox.contains(x, y)) {
            click = 1;
            parent.handleNextTurn();
            session().nextTurn();
        }
        for (int i = 0; i < buttonBox.length; i++) {
            if (buttonBox[i].contains(x, y)) {
                clickButton(i);
                click = 2;
            }
        }
        if (click == 2)
            buttonClick();
        else if (click == 1)
            buttonClick();
    }
    
    @Override
    public void mouseDragged(MouseEvent arg0) { }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (!parent.enableButtons())
            return;
        
        Shape prevHover = hoverBox;
        hoverBox = null;

        if (nextTurnBox.contains(x, y))
            hoverBox = nextTurnBox;

        for (int i = 0; i < buttonBox.length; i++) {
            if (buttonBox[i].contains(x, y))
                hoverBox = buttonBox[i];
        }

        if (prevHover != hoverBox)
            repaint();
    }
}
