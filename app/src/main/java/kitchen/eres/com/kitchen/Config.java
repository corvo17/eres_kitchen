package kitchen.eres.com.kitchen;

public enum  Config {
    Unknown(0),
    ERESStarted(1),// Система ERES стартовала !
    ERESFinished(2),// Система ERES завершает работу, верните смартфоны !
    OrderSendToKitchen(3), // заказ отправлен на кухню
    OrderChangedInWaiter(4), // официант изменил заказ и отправил на кухню
    OrderProblemsInKithen(5), // кухня предложила изменить заказ -- проблемы в кухне
    OrderRejectedInKithen(6), // кухня заказ отвергла
    OrderAcceptedInKitchen(7),// кухня приняла заказ на исполнение
    OrderSentToCashier(8), // счет отправлен на кассу
    OrderSuccessfullyClosed(9), // счет успешно закрыт
    OrderUnSuccessfullyClosed(10), // счет неуспешно закрыт
    CompleteKitchen(11),// кухня выполнила заказ
    OrderIsCanceled(12),// заказ отменен
    OrderIsClosed(13),// заказ закрыт
    OrderTransfer(14),// заказ передан другому официанту
    TableIsServiced(15), // Стол или часть стола взять на обслужывание
    TableIsNotServiced(16),// Стол необслужывается
    TableBooked(17),// Стол забронирован
    TableIsFree(18),// Стол свободен
    HallBooked(19),// Зал забронирован
    HallIsFree(20),// Зал свободен
    HallClosed(21),// Зал закрыт
    KitchenOpen(22),// кухня открылась
    KitchenClosed(23), // кухня закрылась
    MenuHasChanged(24), // меню изменилась
    WaiterStartedWork(25),// официант начал работу
    WaiterFinishedWork(26),// официант закончил работу
    WaiterToManager(27),// официант вызывается к менеджеру
    WaiterToTable(28),// официант вызывается ко столу
    WaiterCheckPlease(29),// официант счет пожалуйста
    AttentionNeedsHelp(30), // внимание, требуется помощь
    AttentionManagerHere(31),// Внимание! Менеджер на борту!
    MenuChanged(32)//Меню изменился
    ;

    Config(int i) {
    }
}
