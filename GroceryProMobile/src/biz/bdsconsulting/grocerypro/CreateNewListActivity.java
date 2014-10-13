/**
 * 
 */
package biz.bdsconsulting.grocerypro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import pl.polidea.treeview.InMemoryTreeStateManager;
import pl.polidea.treeview.R;
import pl.polidea.treeview.TreeBuilder;
import pl.polidea.treeview.TreeStateManager;
import pl.polidea.treeview.TreeViewList;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import biz.bdsconsulting.grocerypro.adapter.FancyColouredVariousSizesAdapter;
import biz.bdsconsulting.grocerypro.adapter.SimpleStandardAdapter;

/**
 * @author kerry
 *
 */
public class CreateNewListActivity extends Activity {
	private static String TAG = "CreateNewListActivity";
	
    private enum TreeType implements Serializable {
        SIMPLE,
        FANCY
    }

    private TreeViewList treeView;
    private final Set<Long> selected = new HashSet<Long>();

    private static final int[] DEMO_NODES = new int[] { 0, 0, 1, 1, 1, 2, 2, 1,
        1, 2, 1, 0, 0, 0, 1, 2, 3, 2, 0, 0, 1, 2, 0, 1, 2, 0, 1 };
	
    private static final int LEVEL_NUMBER = 4;
    private TreeStateManager<Long> manager = null;
    private FancyColouredVariousSizesAdapter fancyAdapter;
    private SimpleStandardAdapter simpleAdapter;
    private TreeType treeType;
    private boolean collapsible;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TreeType newTreeType = null;
        boolean newCollapsible;
        if (savedInstanceState == null) {
            manager = new InMemoryTreeStateManager<Long>();
            final TreeBuilder<Long> treeBuilder = new TreeBuilder<Long>(manager);
            for (int i = 0; i < DEMO_NODES.length; i++) {
                treeBuilder.sequentiallyAddNextNode((long) i, DEMO_NODES[i]);
            }
            Log.d(TAG, manager.toString());
            newTreeType = TreeType.SIMPLE;
            newCollapsible = true;
        } else {
            manager = (TreeStateManager<Long>) savedInstanceState
                    .getSerializable("treeManager");
            newTreeType = (TreeType) savedInstanceState
                    .getSerializable("treeType");
            newCollapsible = savedInstanceState.getBoolean("collapsible");
        }
        setContentView(R.layout.main_demo);
        treeView = (TreeViewList) findViewById(R.id.mainTreeView);
        fancyAdapter = new FancyColouredVariousSizesAdapter(this, selected,
                manager, LEVEL_NUMBER);
        simpleAdapter = new SimpleStandardAdapter(this, selected, manager,
                LEVEL_NUMBER);
        setTreeAdapter(newTreeType);
        setCollapsible(newCollapsible);
        registerForContextMenu(treeView);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable("treeManager", manager);
        outState.putSerializable("treeType", treeType);
        outState.putBoolean("collapsible", this.collapsible);
        super.onSaveInstanceState(outState);
    }

    protected final void setTreeAdapter(final TreeType newTreeType) {
        this.treeType = newTreeType;
        switch (newTreeType) {
        case SIMPLE:
            treeView.setAdapter(simpleAdapter);
            break;
        case FANCY:
            treeView.setAdapter(fancyAdapter);
            break;
        default:
            treeView.setAdapter(simpleAdapter);
        }
    }

    protected final void setCollapsible(final boolean newCollapsible) {
        this.collapsible = newCollapsible;
        treeView.setCollapsible(this.collapsible);
    }

}
