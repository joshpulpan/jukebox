package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Returns the actual time of midnight tomorrow.
 *
 * @author Kenneth Mecum <kmecum@email.arizona.edu>
 * @author Joshua Pulpan <jpulpan@email.arizona.edu>
 */
public class RealDateService implements DateService {

	@Override
	public LocalDateTime getMidnight() {
		return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
	}
}
