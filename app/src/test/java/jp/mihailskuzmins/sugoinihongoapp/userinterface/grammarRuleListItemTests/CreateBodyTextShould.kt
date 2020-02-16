package jp.mihailskuzmins.sugoinihongoapp.userinterface.grammarRuleListItemTests

import jp.mihailskuzmins.sugoinihongoapp.resources.AppConstants
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class CreateBodyTextShould: GrammarRuleListItemTestsBase() {

	@Test
	fun `not add three dots if length is less than max allowed`() {
		val body = (0 until  AppConstants.Models.grammarRuleListItemBodyMaxLength)
			.map { 'a' }
			.joinToString(separator = "") { it.toString() }

		 createClass()
			.createBodyText(body)
			.shouldBe(body)
			 .length
			 .shouldBe(body.length)
	}

	@Test
	fun `add three dots if length is greater than max allowed`() {
		val threeDots = "..."

		val body = (0..AppConstants.Models.grammarRuleListItemBodyMaxLength + 1)
			.map { 'a' }
			.joinToString(separator = "") { it.toString() }

		val expectedResult = body
			.substring(0 until AppConstants.Models.grammarRuleListItemBodyMaxLength)
			.plus(threeDots)

		createClass()
			.createBodyText(body)
			.shouldBeEqualTo(expectedResult)
			.length
			.shouldBe(AppConstants.Models.grammarRuleListItemBodyMaxLength + threeDots.length)
	}

	@Test
	fun `remove white spaces`() {
		val body = "a aa  aaa"
			.plus(System.lineSeparator())
			.plus("aaaa")

		val expectedResult = "a aa aaa aaaa"

		createClass()
			.createBodyText(body)
			.shouldBeEqualTo(expectedResult)
	}

	@Test
	fun `trim body`() {
		val expectedResult = "aaa"
		val body = "  $expectedResult  "

		createClass()
			.createBodyText(body)
			.shouldBeEqualTo(expectedResult)
	}
}