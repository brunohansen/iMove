package br.com.bhansen.test;

import java.util.function.Predicate;

public class TestCase {

	public interface TestPredicate<T> extends Predicate<T> {
		
		public String getText(T expected);
	}
	
	public interface TestPredicateFactory<T> {
		
		public TestPredicate<T> create(T result);
	}
	
	public <T> void checkThat(String text, T expected, T result, TestPredicateFactory<T> factory) {
		checkThat(text, expected, factory.create(result));
	}
	
	public <T> void checkThat(String text, T expected, TestPredicate<T> predicate) {
		if(predicate.test(expected)) {
			System.out.println("\tPASS " + text + " " + predicate.getText(expected));
		} else {
			System.out.println("FAIL " + text + " " + predicate.getText(expected));
		}
	}
	
	public <T> TestPredicateFactory<T> equalTo() {
		return new TestPredicateFactory<T>() {

			@Override
			public TestPredicate<T> create(Object result) {
				return new TestPredicate<T>(){

					@Override
					public boolean test(T expected) {
						return expected.equals(result);
					}

					@Override
					public String getText(T expected) {
						return expected + " == " + result;
					}
					
				};
			}
			
		};
	}
	
	public <T> TestPredicateFactory<T> notEqualTo() {
		return new TestPredicateFactory<T>() {

			@Override
			public TestPredicate<T> create(Object result) {
				return new TestPredicate<T>(){

					@Override
					public boolean test(T expected) {
						return ! expected.equals(result);
					}

					@Override
					public String getText(T expected) {
						return expected + " != " + result;
					}
					
				};
			}
			
		}; 
	}
	
	public <T extends Number> TestPredicateFactory<T> lessThanOrEqualTo() {
		return new TestPredicateFactory<T>() {

			@Override
			public TestPredicate<T> create(T result) {
				return new TestPredicate<T>(){

					@Override
					public boolean test(T expected) {
						return expected.doubleValue() <= result.doubleValue();
					}

					@Override
					public String getText(T expected) {
						return expected + " <= " + result;
					}
					
				};
			}
			
		}; 
	}

}
