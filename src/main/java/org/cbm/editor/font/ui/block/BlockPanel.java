package org.cbm.editor.font.ui.block;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.block.blocklayer.BlockLayer;
import org.cbm.editor.font.ui.block.droplayer.DropLayer;
import org.cbm.editor.font.ui.block.highlight.HighlightLayer;
import org.cbm.editor.font.ui.block.select.SelectionLayer;
import org.cbm.editor.font.ui.util.BottomToolBar;
import org.cbm.editor.font.ui.util.ZoomSpinner;
import org.cbm.editor.font.ui.video.GlobalVideoEmulationSettings;
import org.cbm.editor.font.ui.video.VideoEmulationSettingsModifiedEvent;
import org.cbm.editor.font.util.Events;

public class BlockPanel extends JPanel
    implements ChangeListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{

    private static final long serialVersionUID = 5163661104051966080L;

    private final JScrollPane scrollPane;
    private final BlockComponent blockComponent;
    private final JToolBar bottomToolBar;
    private final ZoomSpinner zoomSpinner;
    private final JCheckBox palCheckBox;

    private Point zoomPoint = null;
    private Point scrollDrag = null;

    public BlockPanel()
    {
        super(new BorderLayout());

        setOpaque(false);

        blockComponent = new BlockComponent();
        blockComponent.addMouseListener(this);
        blockComponent.addMouseMotionListener(this);
        blockComponent.addMouseWheelListener(this);

        bottomToolBar = new BottomToolBar();
        bottomToolBar.setFloatable(false);
        bottomToolBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        zoomSpinner = new ZoomSpinner();
        zoomSpinner.setFocusable(false);
        zoomSpinner.addChangeListener(this);

        palCheckBox = new JCheckBox();
        palCheckBox.setFocusable(false);
        palCheckBox.addActionListener(this);

        bottomToolBar.add(new JLabel("Zoom "));
        bottomToolBar.add(zoomSpinner);
        bottomToolBar.addSeparator();

        bottomToolBar.add(palCheckBox);
        bottomToolBar.add(new JLabel(" PAL Quality "));

        scrollPane = new JScrollPane(blockComponent);
        scrollPane.setWheelScrollingEnabled(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        //		scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, getBackground().darker()));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomToolBar, BorderLayout.SOUTH);

        Events.bind(Registry.get(GlobalVideoEmulationSettings.class), this);
    }

    public void handleEvent(VideoEmulationSettingsModifiedEvent e)
    {
        BlockComponent blockComponet = getBlockComponent();

        if (blockComponet != null)
        {
            BlockLayer rootLayer = blockComponet.getRootLayer();

            if (rootLayer != null)
            {
                rootLayer.invalidateImage();
            }

            blockComponet.repaint();
        }
    }

    public BlockComponent getBlockComponent()
    {
        return blockComponent;
    }

    public void setRootLayer(BlockLayer layer)
    {
        blockComponent.setRootLayer(layer);
    }

    public void setSelectionLayer(SelectionLayer layer)
    {
        blockComponent.setSelectionLayer(layer);
    }

    public void setDropLayer(DropLayer layer)
    {
        blockComponent.setDropLayer(layer);
    }

    public void setHighlightLayer(HighlightLayer layer)
    {
        blockComponent.setHighlightLayer(layer);
    }

    public void setPopupMenu(JPopupMenu popupMenu)
    {
        blockComponent.setPopupMenu(popupMenu);
    }

    public Point relativeToScrollPane(final Point point)
    {
        return new Point(point.x - scrollPane.getHorizontalScrollBar().getValue(),
            point.y - scrollPane.getVerticalScrollBar().getValue());
    }

    public void relocate(final int x, final int y)
    {
        scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getValue() - x);
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() - y);
    }

    public void zoomIn(final Point zoomPoint)
    {
        this.zoomPoint = zoomPoint;

        zoomSpinner.next();
    }

    public void zoomOut(final Point zoomPoint)
    {
        this.zoomPoint = zoomPoint;

        zoomSpinner.previous();
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    @Override
    public void stateChanged(final ChangeEvent e)
    {
        if (e.getSource() == zoomSpinner)
        {
            SwingUtilities.invokeLater(() -> {
                final double currentZoom = blockComponent.getZoom();
                final double nextZoom = zoomSpinner.getZoom();

                if (currentZoom != nextZoom)
                {
                    final JViewport viewport = scrollPane.getViewport();

                    if (zoomPoint == null)
                    {
                        zoomPoint = new Point(viewport.getWidth() / 2, viewport.getHeight() / 2);
                    }

                    final Point viewPosition = viewport.getViewPosition();
                    final Point positionInPixel = blockComponent.convertFromComponentToPixel(
                        new Point(zoomPoint.x + viewPosition.x, zoomPoint.y + viewPosition.y));

                    blockComponent.setZoom(nextZoom);

                    SwingUtilities.invokeLater(() -> {
                        final Point viewPosition1 = blockComponent.convertFromPixelToComponent(positionInPixel);

                        viewPosition1.x -= zoomPoint.x;
                        viewPosition1.y -= zoomPoint.y;

                        scrollPane.getHorizontalScrollBar().setValue(viewPosition1.x);
                        scrollPane.getVerticalScrollBar().setValue(viewPosition1.y);

                        blockComponent.updatePreferredSize();
                        blockComponent.revalidate();
                        blockComponent.repaint();

                        zoomPoint = null;
                    });
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        BlockComponent blockComponet = getBlockComponent();

        if (blockComponet != null)
        {
            blockComponet.setPal(palCheckBox.isSelected());

            BlockLayer rootLayer = blockComponet.getRootLayer();

            if (rootLayer != null)
            {
                rootLayer.invalidateImage();
            }

            blockComponet.repaint();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (blockComponent.getRootLayer() == null)
        {
            return;
        }

        if (!blockComponent.hasFocus())
        {
            blockComponent.grabFocus();
        }

        final Point point = event.getPoint();
        final int modifiers = event.getModifiers();

        if ((modifiers & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
        {
            scrollDrag = relativeToScrollPane(point);
            event.consume();
            return;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (blockComponent.getRootLayer() == null)
        {
            return;
        }

        if (!blockComponent.hasFocus())
        {
            blockComponent.grabFocus();
        }

        if ((event.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
        {
            final Point point = relativeToScrollPane(event.getPoint());

            final int x = point.x - scrollDrag.x;
            final int y = point.y - scrollDrag.y;

            scrollDrag = point;

            relocate(x, y);

            event.consume();
            return;
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (blockComponent.getRootLayer() == null)
        {
            return;
        }

        if (!blockComponent.hasFocus())
        {
            blockComponent.grabFocus();
        }

        if ((event.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
        {
            if (scrollDrag != null)
            {
                scrollDrag = null;

                event.consume();
                return;
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (!blockComponent.hasFocus())
        {
            blockComponent.grabFocus();
        }

        if (event.getWheelRotation() < 0)
        {
            zoomIn(relativeToScrollPane(event.getPoint()));
        }
        else
        {
            zoomOut(relativeToScrollPane(event.getPoint()));
        }
    }

}
