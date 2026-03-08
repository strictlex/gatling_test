package simulations


import io.github.penolegrus.FakerRussian
import io.github.penolegrus.generators.InnOptions
import io.github.penolegrus.generators.InnOptions.Kind

object Vars {

    fun randomInn(): String{
        val innIndivid  = FakerRussian().inn(InnOptions(Kind.INDIVIDUAL))
//        return (1..12).map {(0..9).random()}.joinToString("")

        return innIndivid
    }


    fun randomFullName(): String {
        val maleFirstNames = listOf("Александр", "Максим", "Сергей", "Андрей",
            "Алексей", "Артём", "Илья", "Кирилл", "Михаил",
            "Никита", "Матвей", "Роман", "Егор", "Тимофей")
        val femaleFirstNames = listOf("Анна", "Мария", "Елена", "Дарья", "Анастасия",
            "Ольга", "Наталья", "Екатерина", "Ксения", "Виктория",
            "Полина", "Татьяна", "Светлана", "Ирина", "Юлия")
        val maleLastNames = listOf(
            "Иванов", "Петров", "Сидоров", "Кузнецов", "Смирнов",
            "Попов", "Лебедев", "Козлов", "Новиков", "Морозов",
            "Волков", "Зайцев", "Соловьёв", "Васильев", "Павлов")
        val femaleLastNames = maleLastNames.map {
            when {
                it.endsWith("ов") -> it.replace("ов", "ова")
                it.endsWith("ёв") -> it.replace("ёв", "ёва")
                else -> it + "а"
            }
        }
        val malePatronymics = listOf(
            "Александрович", "Дмитриевич", "Максимович", "Сергеевич", "Андреевич",
            "Алексеевич", "Артёмович", "Ильич", "Кириллович", "Михайлович",
            "Никитич", "Матвеевич", "Романович", "Егорович", "Тимофеевич")
        val femalePatronymics = malePatronymics.map {
            when {
                it.endsWith("ич") -> it.replace("ич", "на")
                it.endsWith("ович") -> it.replace("ович", "овна")
                it.endsWith("евич") -> it.replace("евич", "евна")
                else -> it + "на"
            }
        }
        val gender = listOf(maleFirstNames,femaleFirstNames).random()
        val fullName = if (gender == maleFirstNames) "${maleLastNames.random()} ${maleFirstNames.random()} ${malePatronymics.random()}"
        else "${femaleLastNames.random()} ${femaleFirstNames.random()} ${femalePatronymics.random()}"


        return fullName
    }
}