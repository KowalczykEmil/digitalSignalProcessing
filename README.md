
# digitalSignalProcessing

##  :low_brightness: Przetwarzanie sygnałów.

Aplikacja została napisana w języku JAVA, posiada interfejs graficzny napisany w JavaFX. <br>
Jej głównym zadaniem jest dostarczenie funkcjonalności w celu 
przetwarzania różnego rodzaju sygnałów. 

### Obecne funkcjonalności programu: 
* Generowanie sygnału 
* Utworzony histogram, podczas wygenerowania sygnału.
* Obliczona charakterystyka sygnału ( wartość średnia, bezwględna, moc średnia, wariancja , wartość skuteczna)
* Możliwe operacje arytmetyczne (dodawanie, odejmowanie, mnożenie, dzielenie sygnałów, splot, korelacja, korelacja z impl. splotu)
* Zapis / Odczyt wykresu do/z pliku binarnego.
* Modyfikacja sygnału, przetworzenie A/C (próbkowanie, kwantyzacja równomierna z zaokrągleniem, ekstrapolacja zerowego rzędu, rekonstrukcja w oparciu o funkcje sinc)
* Porównanie sygnałów.
* Filtrowanie sygnałów (Filtr Dolnoprzepustowy / Górnoprzepustowy) 
* Pomiar odległości (Doświadczenie z sygnałem odbitym od przeszkody)
* Dyskretna transformacja Fouriera (DFT)

W wersji pierwszej programu (branch: CPS_V1) utworzyłem bazę programu w celu jego dalszego rozwijania. 
Wówczas można było wygenerować sygnał, przejrzeć jego histogram, w którym można zauważyć ilość próbek sygnału w danym zakresie czasu, oraz charakterystykę.

W wersji drugiej programu (branch: CPS_V2) dodałem zakładkę: "Operacje 2", można w niej:
* próbkować sygnał
* skwantyzować równomiernie z zaokrągleniem
* wykorzystać ekstrapolacje zerwoego rzędu.
* rekonstruować sygnał w oparciu o funkcje sinc.

oraz zakładkę: "Porównanie", która przydaje się do zweryfikowania tego jak zmienił się nasz sygnał. Porównanie działa na zasazie nakładania się sygnałów.


W finalnej wersji programu (branch: CPS_Final), dodałem do programu filtr dolno i górno przepustowy oraz operację SPLOTu sygnału i korelację.
Dodatkiem była również funkcjonalność pomiaru odległości, gdzie za pomocą korelacji sygnałów, można utworzyć doświadczenie, w którym utworzony sygnał, odbije się od przeszkody,
następnie wracając w miejsce z którego ruszał. Dzięki korelacji i wcześniej ustawionych argumentów, można zobaczyć, jak dany sygnał się zmienił podczas odbicia od przeszkody, oraz oblcizyć odległość przeszkody od sygnału, co za tym idzie
również szybkość z jakim sygnał na daną przeszkodę natrafił. Taki pomiar odległości zaprezentowany jest na Obrazie X. 

Ostatnią implementacją w programie była funkcjonalność przetworzenia sygnału za pomocą dyskretnej transformacji Fouriera (DFT). 

<p align="center">
  <img src="resources/F_wyglad.png"> <br>
  <b>Obraz 1.</b> Wygląd aplikacji
</p>


### :hammer_and_wrench: Dośw. 1  -  Zastosowanie filtru dolnoprzepustowego w celu wytłumienia składowej o wysokiej częstotliwości.
Na samym początku wygenerowałem dwa sygnały sinusoidalne o częstotliwości 100 Hz oraz 20 Hz, następnie je do siebie dodałem. 
W celu zaprezentowania działania fitlru, chcę wygasić z sumowanego sygnału częstotliwości 100 Hz, tak, żeby widoczna była składowa 20 Hz.

Wykorzystałem filtr dolnoprzepustowy, rząd filtra czyli liczbę próbek odpowiedzi impulsowej filtra ustawiłem na wysoką wartość 191, im wyższa wartość, tym dążymy do filtru idealnego.
Dlaczego (?) -> A no dlatego, że wykorzystamy znacznie większą złożoność obliczeniową, a spadek na wykresie zacznie się najbliżej miejsca odcięcia, co spowoduje, że zmniejszy się ryzyko na brak stłumienia składowej, którą chcemy zarzymać.

<p align="center">
  <img src="resources/F_sinus_20.png"> <br>
  <b>Obraz 2.</b> Wykres sygnału sinusoidalnego o częstotliwości 20 Hz
</p>

<p align="center">
  <img src="resources/F_sinus_100.png"> <br>
  <b>Obraz 3.</b> Wykres sygnału sinusoidalnego o częstotliwości 100 Hz
</p>

<p align="center">
  <img src="resources/F_dodanie.png"> <br>
  <b>Obraz 4.</b> Dodanie dwóch sygnałów sinusoidalnych (100 Hz + 20 Hz).
</p>

<p align="center">
  <img src="resources/filtry.png"> <br>
  <b>Obraz 5.</b> Wykorzystany do doświadczenia - Filtr dolnoprzepustowy
</p>

<p align="center">
  <img src="resources/odpowiedzImpulsowa.png"> <br>
  <b>Obraz 6.</b> Otrzymana odpowiedź impulsowa, po wykorzystaniu filtra dolnoprzepustowego.
</p>

Odpowiedź impulsową, którą otrzymałem po wykorzystaniu filtra dolnoprzepustowego na zsuomowanym sygnale (Obraz 4), muszę teraz spleść z sumowanym sygnałem.
W przypadku tej wersji programu, najpierw musiałem spróbkować zsumowany sygnał (Obraz 4), żeby splot wykonany był na tym samym typie sygnału (dyskretnym).

<p align="center">
  <img src="resources/wytlumienie100hz.png"> <br>
  <b>Obraz 7.</b> Wynik zastosowanego filtru dolnoprzepustowego, na zsumowanym sygnale
</p>

Widać, że sygnał się zmienił. Czy tego oczekiwałem? Tak, na kolejnym obrazku gdzie wykonałem porównanie sygnału zsumowanego z tym finalnym (obraz  ), będzie można zaobserwować, że udało się wytłumić z sygnału składową 100 Hz, a została jedynie składowa o częst. 20 Hz

<p align="center">
  <img src="resources/porownaniewytlumienia.png"> <br>
  <b>Obraz 8.</b> Porównanie sygnału zsumowanego z sygnałem na którym zastosowaliśmy filtr dolnoprzepustowy
</p>

### Wniosek - dośw. 1
Filtr dolnoprzepustowy zadziałał prawidłowo, zmniejszył amplitudę wykresu, oraz ze względu na wysoko ustawiony rząd filtra, wytłumienie składowej 100 Hz z sygnału, jest naprawdę satysfakcjonujące. Gdybym ustawił mniejszy rząd filtra, np: 80 również bym wytłumił składową 100 Hz, natomiast sam efekt nie byłby tak "idealny", mówiąc krótko, mógłbym na sygnale zauważąć nadal pewne falowania mówiące o widoczności drugiej składowej, natomiast nadal byłby to wynik satysfakcjonujący.
Wartość miejsca odcięcia ustawiłem na 50, tak żeby znajdowała się pomiędzy jedną a drugą składową. Akceptowalne byłyby tu wartości od 40 do 60.
