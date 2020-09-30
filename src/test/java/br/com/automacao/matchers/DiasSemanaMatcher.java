/**
 * 
 */
package br.com.automacao.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.com.automacao.utils.DataUtils;

/**
 * @author zupper
 *
 */
public class DiasSemanaMatcher extends TypeSafeMatcher<Date> {
	
	private Integer diaSemana;

	public DiasSemanaMatcher(Integer diaSemana) {
		this.diaSemana = diaSemana;

	}
	
	public void describeTo(Description desc) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK, diaSemana);
		String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt","BR"));
		desc.appendText(dataExtenso);
	}

	@Override
	protected boolean matchesSafely(Date data) {
		
		return DataUtils.verificarDiaSemana(data, diaSemana);
	}

}
