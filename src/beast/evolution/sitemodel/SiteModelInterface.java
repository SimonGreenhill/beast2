package beast.evolution.sitemodel;

import java.util.ArrayList;
import java.util.List;

import beast.core.CalculationNode;
import beast.core.Description;
import beast.core.Input;
import beast.core.StateNode;
import beast.core.Input.Validate;
import beast.evolution.datatype.DataType;
import beast.evolution.substitutionmodel.HKY;
import beast.evolution.substitutionmodel.SubstitutionModel;
import beast.evolution.tree.Node;


/**
 * SiteModel - Specifies how rates and substitution models vary across sites.
 *
 * @author Andrew Rambaut
 * @author Alexei Drummond
 *
 * @version $Id: SiteModel.java,v 1.77 2005/05/24 20:25:58 rambaut Exp $
 */

public interface SiteModelInterface {

	    /**
	     * Get this site model's substitution model
	     * @return the substitution model
	     */
	    SubstitutionModel getSubstitutionModel();

	    /**
		 * Specifies whether SiteModel should integrate over the different categories at
		 * each site. If true, the SiteModel will calculate the likelihood of each site
		 * for each category. If false it will assume that there is each site can have a
		 * different category.
	     * @return the boolean
	     */
		boolean integrateAcrossCategories();

		/**
		 * @return the number of categories of substitution processes
		 */
		int getCategoryCount();

		/**
		 * Get the category of a particular site. If integrateAcrossCategories is true.
		 * then throws an IllegalArgumentException.
		 * @param site the index of the site
		 * @return the index of the category
		 */
		int getCategoryOfSite(int site, Node node);

		/**
		 * Get the rate for a particular category. This will include the 'mu' parameter, an overall
	     * scaling of the siteModel.
		 * @param category the category number
		 * @return the rate.
		 */
		double getRateForCategory(int category, Node node);

	    /**
	     * Get an array of the rates for all categories.
	     * @return an array of rates.
	     */
	    double[] getCategoryRates(Node node);

		/**
		 * Get the expected proportion of sites in this category.
		 * @param category the category number
		 * @return the proportion.
		 */
		double getProportionForCategory(int category, Node node);

		/**
		 * Get an array of the expected proportion of sites for all categories.
		 * @return an array of proportions.
		 */
		double[] getCategoryProportions(Node node);

		
		/** set DataType so it can validate the Substitution model can handle it **/
		void setDataType(DataType dataType);
		
		
		@Description(value = "Non-functional base implementation of a site model", isInheritable=false)
		public abstract class Base extends CalculationNode implements SiteModelInterface {
		    public Input<SubstitutionModel.Base> m_pSubstModel =
	            new Input<SubstitutionModel.Base>("substModel", "substitution model along branches in the beast.tree", new HKY(), Validate.REQUIRED);

		    public boolean canSetSubstModel(Object o) throws Exception {
				SubstitutionModel substModel = (SubstitutionModel) o;
		    	if (m_dataType != null) {
		    		if (!substModel.canHandleDataType(m_dataType)) {
		    			throw new Exception("substitution model cannot handle data type");
		    		}
		    	}
		    	return true;
			}

		    DataType m_dataType;
		    
			@Override
			public SubstitutionModel getSubstitutionModel() {
		        return m_pSubstModel.get();
			}

			
			/** list of IDs onto which SiteModel is conditioned **/
			protected List<String> conditions = null;
			
			/** return the list, useful for ... **/
		    public List<String> getConditions() {
		        return conditions;
		    }
		    
		    /** add item to the list **/
		    public void addCondition(Input<? extends StateNode> stateNode) {
		        if (stateNode.get() == null) return;

		        if (conditions == null) conditions = new ArrayList<String>();

		        conditions.add(stateNode.get().getID());
		    }
		
			public void setDataType(DataType dataType) {
				m_dataType = dataType;
			}
			
		} // class SiteModelInterface.Base 
		
} // SiteModelInterface