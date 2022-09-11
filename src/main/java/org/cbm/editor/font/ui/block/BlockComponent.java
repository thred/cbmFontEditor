package org.cbm.editor.font.ui.block;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.ui.block.blocklayer.BlockLayer;
import org.cbm.editor.font.ui.block.droplayer.DropLayer;
import org.cbm.editor.font.ui.block.highlight.HighlightLayer;
import org.cbm.editor.font.ui.block.select.Selection;
import org.cbm.editor.font.ui.block.select.SelectionLayer;
import org.cbm.editor.font.util.Events;
import org.cbm.editor.font.util.Palette;

public class BlockComponent extends JComponent implements MouseListener, MouseMotionListener, FocusListener, KeyListener
{

    private static final long serialVersionUID = 7070091179282391631L;

    private BlockLayer rootLayer;
    private SelectionLayer selectionLayer;
    private DropLayer dropLayer;
    private HighlightLayer highlightLayer;
    private JPopupMenu popupMenu;
    private double zoom = 1;
    private boolean pal = false;
    private Tool previousTool;

    public BlockComponent()
    {
        super();

        setBackground(Color.BLACK);

        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
        addKeyListener(this);

        setTransferHandler(new BlockComponentTransferHandler());
    }

    public boolean isPal()
    {
        return pal;
    }

    public void setPal(boolean pal)
    {
        this.pal = pal;
    }

    public void resized()
    {
        updatePreferredSize();
        revalidate();
        repaint();
    }

    public BlockLayer getRootLayer()
    {
        return rootLayer;
    }

    public void setRootLayer(BlockLayer rootLayer)
    {
        disposeLayer(this.rootLayer);
        this.rootLayer = rootLayer;
        prepareLayer(rootLayer);
    }

    public SelectionLayer getSelectionLayer()
    {
        return selectionLayer;
    }

    public void setSelectionLayer(SelectionLayer selectionLayer)
    {
        disposeLayer(this.selectionLayer);
        this.selectionLayer = selectionLayer;
        prepareLayer(selectionLayer);
    }

    public DropLayer getDropLayer()
    {
        return dropLayer;
    }

    public void setDropLayer(DropLayer dropLayer)
    {
        disposeLayer(this.dropLayer);
        this.dropLayer = dropLayer;
        prepareLayer(dropLayer);
    }

    public HighlightLayer getHighlightLayer()
    {
        return highlightLayer;
    }

    public void setHighlightLayer(HighlightLayer highlightLayer)
    {
        disposeLayer(this.highlightLayer);
        this.highlightLayer = highlightLayer;
        prepareLayer(highlightLayer);
    }

    public JPopupMenu getPopupMenu()
    {
        return popupMenu;
    }

    public void setPopupMenu(JPopupMenu popupMenu)
    {
        this.popupMenu = popupMenu;
        setComponentPopupMenu(popupMenu);
    }

    public double getZoom()
    {
        return zoom;
    }

    public void setZoom(double zoom)
    {
        if (this.zoom != zoom)
        {
            this.zoom = zoom;
        }
    }

    /**
     * Converts a point relative to the whole component, to a pixel on the block
     *
     * @param positionInComponent the point relative to the component
     * @return the point on the block
     */
    public Point convertFromComponentToPixel(final Point positionInComponent)
    {
        return new Point((int) Math.floor(positionInComponent.x / zoom),
            (int) Math.floor(positionInComponent.y / zoom));
    }

    public Point convertFromComponentToCharacter(final Point positionInComponent)
    {
        if (rootLayer == null)
        {
            return new Point(0, 0);
        }

        final Point position = convertFromComponentToPixel(positionInComponent);

        return new Point((int) Math.floor((double) position.x / rootLayer.getCharacterWidth()),
            (int) Math.floor((double) position.y / rootLayer.getCharacterHeight()));
    }

    /**
     * Converts a point relative to the block, to a point (top, left) on the component
     *
     * @param positionInPixel the point
     * @return the point on the component
     */
    public Point convertFromPixelToComponent(final Point positionInPixel)
    {
        return new Point((int) (positionInPixel.x * zoom), (int) (positionInPixel.y * zoom));
    }

    public Rectangle convertFromPixelToComponent(Rectangle rectangleInPixel)
    {
        return new Rectangle((int) (rectangleInPixel.x * zoom), (int) (rectangleInPixel.y * zoom),
            (int) (rectangleInPixel.width * zoom), (int) (rectangleInPixel.height * zoom));
    }

    public Rectangle convertFromPixelToCharacter(Rectangle rectangleInPixel)
    {
        Block block = rootLayer.getBlock();

        int x = (int) Math.floor((double) rectangleInPixel.x / block.getCharacterWidth());
        int y = (int) Math.floor((double) rectangleInPixel.y / block.getCharacterHeight());
        int w = (int) Math.ceil((double) (rectangleInPixel.x + rectangleInPixel.width) / block.getCharacterWidth()) - x;
        int h =
            (int) Math.ceil((double) (rectangleInPixel.y + rectangleInPixel.height) / block.getCharacterHeight()) - y;

        return new Rectangle(x, y, w, h);
    }

    public Point transfer(Point point)
    {
        Point position = calculateBlockPosition(zoom, getSize());

        point.x -= position.x;
        point.y -= position.y;

        return point;
    }

    /**
     * Calculates the top left position of the block image
     *
     * @param zoom the zoom
     * @param size the total size of the component
     * @return the position
     */
    public Point calculateBlockPosition(final double zoom, final Dimension size)
    {
        if (rootLayer == null)
        {
            return new Point(0, 0);
        }

        return new Point( //
            (int) ((size.width - rootLayer.getWidthInPixel() * zoom) / 2), //
            (int) ((size.height - rootLayer.getHeightInPixel() * zoom) / 2));
    }

    public Dimension convertFromBlockToComponent(final Dimension size)
    {
        return new Dimension((int) (size.width * zoom), (int) (size.height * zoom));
    }

    public Point convertFromBlockCharacterToComponent(final Point positionInCharacter)
    {
        if (rootLayer == null)
        {
            return new Point(0, 0);
        }

        return new Point((int) (positionInCharacter.x * rootLayer.getWidthInCharacters() * zoom),
            (int) (positionInCharacter.y * rootLayer.getHeightInCharacters() * zoom));
    }

    public Collection<Point> getCharacterPositions(Point positionInPixel)
    {
        Collection<Point> result = new HashSet<>();

        if (selectionLayer.isInSelection(positionInPixel.x, positionInPixel.y))
        {
            Rectangle rectangle = selectionLayer.getSelection().getRectangle();

            for (int y = 0; y < rectangle.height / 8; y += 1)
            {
                for (int x = 0; x < rectangle.width / 8; x += 1)
                {
                    result.add(new Point(rectangle.x / 8 + x, rectangle.y / 8 + y));
                }
            }
        }
        else
        {
            result.add(new Point(positionInPixel.x / 8, positionInPixel.y / 8));
        }

        return result;
    }

    public BlockSelection createBlockSelection()
    {
        Selection selection = selectionLayer.getSelection();

        if (selection == null)
        {
            return null;
        }

        Rectangle rectangle = selection.getRectangle();

        return rootLayer.createBlockSelection(rectangle);
    }

    void updatePreferredSize()
    {
        if (rootLayer == null)
        {
            setPreferredSize(new Dimension(0, 0));
            return;
        }

        Dimension preferredSize =
            new Dimension((int) ((rootLayer.getWidthInPixel() + 2 * rootLayer.getOverscanInPixel()) * zoom),
                (int) ((rootLayer.getHeightInPixel() + 2 * rootLayer.getOverscanInPixel()) * zoom));

        setPreferredSize(preferredSize);
    }

    public boolean isSelectionAllowed()
    {
        return dropLayer == null || !dropLayer.hasBlockSelection();
    }

    public void updateCursor()
    {
        Cursor cursor = null;

        if (rootLayer != null)
        {
            final Point positionInComponent = transfer(new Point(MouseInfo.getPointerInfo().getLocation()));

            SwingUtilities.convertPointFromScreen(positionInComponent, this);

            cursor = getCursor(positionInComponent, Registry.get(GUIAdapter.class).getTool());
        }

        if (cursor == null)
        {
            cursor = Cursor.getDefaultCursor();
        }

        if (!cursor.equals(getCursor()))
        {
            setCursor(cursor);
        }
    }

    public Cursor getCursor(final Point positionInComponent, final Tool tool)
    {
        Cursor cursor = null;

        if (rootLayer != null)
        {
            cursor = rootLayer.getCursor(positionInComponent, tool);
        }

        if (selectionLayer != null)
        {
            Cursor selectionCursor = selectionLayer.getCursor(positionInComponent, tool);

            if (selectionCursor != null)
            {
                cursor = selectionCursor;
            }
        }

        if (dropLayer != null)
        {
            Cursor dropCursor = dropLayer.getCursor(positionInComponent, tool);

            if (dropCursor != null)
            {
                cursor = dropCursor;
            }
        }

        return cursor;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics graphics)
    {
        Graphics2D g = (Graphics2D) graphics;

        if (rootLayer == null)
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        Palette bottomBackground = rootLayer.getBackground();

        g.setColor(bottomBackground != null ? bottomBackground.getColor() : Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        Point position = calculateBlockPosition(zoom, getSize());

        g.translate(position.x, position.y);

        rootLayer.draw(g);

        if (selectionLayer != null && hasFocus())
        {
            selectionLayer.draw(g);
        }

        if (dropLayer != null)
        {
            dropLayer.draw(g);
        }

        if (highlightLayer != null)
        {
            highlightLayer.draw(g);
        }

        g.translate(-position.x, -position.y);
    }

    private void prepareLayer(Layer layer)
    {
        if (layer == null)
        {
            return;
        }

        layer.setComponent(this);
        Events.bind(layer, this);

        if (layer instanceof MouseListener)
        {
            addMouseListener((MouseListener) layer);
        }

        if (layer instanceof MouseMotionListener)
        {
            addMouseMotionListener((MouseMotionListener) layer);
        }

        if (layer instanceof MouseWheelListener)
        {
            addMouseWheelListener((MouseWheelListener) layer);
        }

        updatePreferredSize();
        revalidate();
        repaint();
    }

    private void disposeLayer(Layer layer)
    {
        if (layer == null)
        {
            return;
        }

        Events.unbind(layer, this);
        layer.setComponent(null);

        if (layer instanceof MouseListener)
        {
            removeMouseListener((MouseListener) layer);
        }

        if (layer instanceof MouseMotionListener)
        {
            removeMouseMotionListener((MouseMotionListener) layer);
        }

        if (layer instanceof MouseWheelListener)
        {
            removeMouseWheelListener((MouseWheelListener) layer);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        // intentionally blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        updateCursor();
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    @Override
    public void focusGained(final FocusEvent e)
    {
        updateCursor();
        repaint();
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    @Override
    public void focusLost(final FocusEvent e)
    {
        if (previousTool != null)
        {
            GUIAdapter guiAdapter = Registry.get(GUIAdapter.class);
            guiAdapter.selectTool(previousTool);
            previousTool = null;
        }

        updateCursor();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
        {
            GUIAdapter guiAdapter = Registry.get(GUIAdapter.class);
            previousTool = guiAdapter.getTool();
            guiAdapter.selectTool(Tool.SELECTION);
            updateCursor();
            repaint();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT && previousTool != null)
        {
            GUIAdapter guiAdapter = Registry.get(GUIAdapter.class);
            guiAdapter.selectTool(previousTool);
            previousTool = null;
            updateCursor();
            repaint();
        }
    }

}
