package tests;

import java.time.LocalDateTime;

import model.DateService;

/**
 * A mock date service. "Midnight" is always 3 seconds from the call so tests
 * can check that things change properly.
 */
public class MockDateService implements DateService {

	@Override
	public LocalDateTime getMidnight() {
		return LocalDateTime.now().plusSeconds(3);
	}
}
