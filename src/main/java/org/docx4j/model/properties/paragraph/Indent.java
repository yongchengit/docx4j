/*
 *  Copyright 2009, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */
package org.docx4j.model.properties.paragraph;

import java.math.BigInteger;

import org.docx4j.UnitsOfMeasurement;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.Ind;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class Indent extends AbstractParagraphProperty {
	
	// Just handles left for the moment
	
	public final static String CSS_NAME = "margin-left";  // Use 'margin-left' instead of 'left' for CSS.
	// 'Left' pushes the box to the right, which results can result in a horizontal scroll bar in the web browser.
	
	public final static String FO_NAME  = "start-indent"; 
	
	
	public Indent(Ind val) {
		this.setObject(val);
	}
	
	public Indent(CSSValue value) {	
		
		debug(CSS_NAME, value);
		
		Ind ind = Context.getWmlObjectFactory().createPPrBaseInd();
		
		CSSPrimitiveValue cssPrimitiveValue = (CSSPrimitiveValue)value;	
		short ignored = 1;
		float fVal = cssPrimitiveValue.getFloatValue(ignored); // unit type ignored in cssparser

		int twip;
		
		short type = cssPrimitiveValue.getPrimitiveType();
		if (CSSPrimitiveValue.CSS_IN == type) {
			twip = UnitsOfMeasurement.inchToTwip(fVal);
		} else if (CSSPrimitiveValue.CSS_MM == type) {
			twip = UnitsOfMeasurement.mmToTwip(fVal);		
		} else {
			log.error("No support for unit " + type);
			twip = 0;
		}
		ind.setLeft(BigInteger.valueOf(twip) );		
		this.setObject(ind);
		
	}

	@Override
	public String getCssProperty() {
		
		BigInteger left = ((Ind)this.getObject()).getLeft();		
		if (left==null) {
			log.warn("Only left indentation is handled at present");
			return CSS_NULL;
		} else {
			return "position: relative; " + composeCss(CSS_NAME, UnitsOfMeasurement.twipToBest(left.intValue()) );
		} 
		
	}
	

	@Override
	public void setXslFO(Element foElement) {

		BigInteger left = ((Ind)this.getObject()).getLeft();		
		if (left==null) {
			log.warn("Only left indentation is handled at present");
		} else {
			foElement.setAttribute(FO_NAME, UnitsOfMeasurement.twipToBest(left.intValue()) );
		} 		
	}

	@Override
	public void set(PPr pPr) {
		pPr.setInd( (Ind)this.getObject() );
	}
	
}