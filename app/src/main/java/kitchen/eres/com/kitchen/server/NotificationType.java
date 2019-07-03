package kitchen.eres.com.kitchen.server;

public enum NotificationType
{
    Unknown,
    ERESStarted,// Система ERES стартовала !
    ERESFinished,// Система ERES завершает работу, верните смартфоны !
    OrderSendToKitchen, // заказ отправлен на кухню 111
    OrderChangedInWaiter, // официант изменил заказ и отправил на кухню
    OrderProblemsInKithen, // кухня предложила изменить заказ -- проблемы в кухне 22
    OrderRejectedInKithen, // кухня заказ отверглаv 3333
    OrderAcceptedInKitchen,// кухня приняла заказ на исполнение 44444
    OrderSentToCashier, // счет отправлен на кассу
    OrderSuccessfullyClosed, // счет успешно закрыт
    OrderUnSuccessfullyClosed, // счет неуспешно закрыт
    CompleteKitchen,// кухня выполнила заказ 5555
    OrderIsCanceled,// заказ отменен
    OrderIsClosed,// заказ закрыт
    OrderTransfer,// заказ передан другому официанту
    TableIsServiced, // Стол или часть стола взять на обслужвание
    TableIsNotServiced,// Стол необслужывается
    TableBooked,// Стол забронирован
    TableIsFree,// Стол свободен
    HallBooked,// Зал забронирован
    HallIsFree,// Зал свободен
    HallClosed,// Зал закрыт
    KitchenOpen,// кухня открылась
    KitchenClosed, // кухня закрылась
    MenuHasChanged, // меню изменился
    WaiterStartedWork,// официант начал работу
    WaiterFinishedWork,// официант закончил работу
    WaiterToManager,// официант вызывается к менеджеру
    WaiterToTable,// официант вызывается ко столу
    WaiterCheckPlease,// официант счет пожалуйста
    AttentionNeedsHelp, // внимание, требуется помощь
    AttentionManagerHere,// Внимание! Менеджер на борту!
    MenuChanged,//Меню изменился
    AddMyPhone, // Добавь моё устройство
    DeviceIsRegistered, // Устройство зарегистрировано
    DeviceIsUnRegistered, // Устройство зарегистрировано
    OrderPleasePrint, // Пожалуйста распечатайте ордер
    OrderIsPrinted    // Ордер распечатан

}
