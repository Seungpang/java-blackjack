package blackjack.domain.participant;

import static blackjack.domain.TestCardFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.card.Hand;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    @DisplayName("딜러가 정상적으로 생성되는지 확인")
    void create() {
        Dealer dealer = new Dealer(new Hand());
        assertThat(dealer).isNotNull();
    }

    @Test
    @DisplayName("딜러가 카드를 정상적으로 주는지 확인")
    void drawCard() {
        Deck deck = new Deck(List.of(jackCard, sixCard));
        Dealer dealer = new Dealer(new Hand(), deck);
        Card card = dealer.drawCard();

        assertThat(card.getDenomination()).isEqualTo("J");
    }

    @Test
    @DisplayName("딜러는 카드의 수가 16이하 일 때 한 장의 카드를 더 받는다")
    void dealerReceiveCard() {
        Deck deck = new Deck(List.of(jackCard, sixCard));
        Dealer dealer = new Dealer(new Hand(), deck);
        dealer.selfDraw();
        dealer.selfDraw();

        Assertions.assertThat(dealer.shouldReceive()).isTrue();
    }

    @Test
    @DisplayName("딜러는 카드의 수가 17이상일 떄 카드를 받을 수 없다")
    void dealerCannotReceiveCard() {
        Deck deck = new Deck(List.of(jackCard, sevenCard));
        Dealer dealer = new Dealer(new Hand(), deck);
        dealer.selfDraw();
        dealer.selfDraw();

        Assertions.assertThat(dealer.shouldReceive()).isFalse();
    }

    @Test
    @DisplayName("딜러는 자신의 카드 한장을 정상적으로 오픈 하는지 확인")
    void openDealerCard() {
        Deck deck = new Deck(List.of(jackCard, aceCard));
        Dealer dealer = new Dealer(new Hand(), deck);
        dealer.selfDraw();
        dealer.selfDraw();

        assertThat(dealer.getOpenCard()).isEqualTo(jackCard);
    }

    @Test
    @DisplayName("플레이어들에게 2장씩 기본 카드 세팅을 해주는 확인")
    void drawCardToPlayers() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("승팡");
        Player p2 = new Player("필즈");
        players.add(p1);
        players.add(p2);

        Dealer dealer = new Dealer(new Hand());

        dealer.drawCardToPlayers(players);
        assertAll(
            () -> assertThat(p1.getCards().size()).isEqualTo(2),
            () -> assertThat(p2.getCards().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("플레이어에게 카드 한장을 정상적으로 주는지 확인")
    void giveCard() {
        Dealer dealer = new Dealer(new Hand(), new Deck(List.of(jackCard, aceCard)));
        Player player = new Player("승팡");

        dealer.giveCard(player);
        assertThat(player.getCards()).containsExactly(jackCard);
    }

    @Test
    @DisplayName("딜러를 생성할때 덱이 null을 포함한채 생성하면 예외발생")
    void validateOrNull() {
        Name name = new Name("승팡");
        Hand cardHand = new Hand();

        assertThatThrownBy(() -> new Dealer(name, cardHand, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("[ERROR] 덱은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("딜러를 생성할때 이름이 null을 포함한채 생성하면 예외발생")
    void validateNameIsNull() {
        Hand cardHand = new Hand();
        Deck deck = Deck.createShuffledCards();

        assertThatThrownBy(() -> new Dealer(null, cardHand, deck))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("[ERROR] 이름과 카드패가 null일 수 없습니다.");
    }
}
