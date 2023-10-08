package hk.hku.cs.card24

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.singularsys.jep.EvaluationException
import com.singularsys.jep.Jep
import com.singularsys.jep.ParseException
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var rePick: Button? = null
    private var checkInput: Button? = null
    private var clear: Button? = null
    private var left: Button? = null
    private var right: Button? = null
    private var plus: Button? = null
    private var minus: Button? = null
    private var multiply: Button? = null
    private var divide: Button? = null
    private lateinit var expression: TextView
    private lateinit var cards: Array<ImageButton>

    private lateinit var data: Array<Int> // for storing 1, 2, …, 13
    private lateinit var card: Array<Int> // for storing 1, 2, …, 52
    private lateinit var imageCount: Array<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rePick = findViewById<Button>(R.id.repick)
        checkInput = findViewById<Button>(R.id.checkinput)
        left = findViewById<Button>(R.id.left)
        right = findViewById<Button>(R.id.right)
        plus = findViewById<Button>(R.id.plus)
        minus = findViewById<Button>(R.id.minus)
        multiply = findViewById<Button>(R.id.multiply)
        divide = findViewById<Button>(R.id.divide)
        clear = findViewById<Button>(R.id.clear)
        expression = findViewById<TextView>(R.id.input)
        cards = arrayOf(findViewById<ImageButton>(R.id.card1),
            findViewById<ImageButton>(R.id.card2),
            findViewById<ImageButton>(R.id.card3),
            findViewById<ImageButton>(R.id.card4))


        cards[0].setOnClickListener(View.OnClickListener { clickCard(0) })
        cards[1].setOnClickListener(View.OnClickListener { clickCard(1) })
        cards[2].setOnClickListener(View.OnClickListener { clickCard(2) })
        cards[3].setOnClickListener(View.OnClickListener { clickCard(3) })

        imageCount = arrayOf(0, 0, 0, 0)
        initCardImage()
        pickCard()


        left!!.setOnClickListener { expression.append("(") }
        right!!.setOnClickListener { expression.append(")") }
        plus!!.setOnClickListener { expression.append("+") }
        minus!!.setOnClickListener { expression.append("-") }
        multiply!!.setOnClickListener { expression.append("*") }
        divide!!.setOnClickListener { expression.append("/") }
        clear!!.setOnClickListener { setClear() }
        checkInput!!.setOnClickListener {
            val inputStr: String = expression.text.toString()
            if (checkInput(inputStr)) {
                Toast.makeText(
                    this@MainActivity, "Correct Answer",
                    Toast.LENGTH_SHORT
                ).show()
                pickCard()
            } else {
                Toast.makeText(
                    this@MainActivity, "Wrong Answer",
                    Toast.LENGTH_SHORT
                ).show()
                setClear()
            }
        }

    }

    private fun initCardImage() {
        for (i in 0..3) {
            val resID: Int = resources.getIdentifier("back_0", "drawable", "hk.hku.cs.card24")
            cards[i].setImageResource(resID)
        }
    }

    private fun pickCard(){
        data = arrayOf(0, 0, 0, 0)
        card = arrayOf(0, 0, 0, 0)
        card[0] = 4
        card[1] = 5
        card[2] = 9
        card[3] = 10
        data[0] = 4
        data[1] = 5
        data[2] = 9
        data[3] = 10
        setClear()

    }

    private fun setClear() {
        var resID: Int
        expression.text = ""
        for (i in 0..3) {
            imageCount[i] = 0
            resID = resources.getIdentifier("card" + card[i], "drawable", "hk.hkucs.card24")
            cards[i].setImageResource(resID)
            cards[i].isClickable = true
        }
    }
    private fun clickCard(i: Int) {
        val resId: Int
        val num: String
        val value: Int
        if (imageCount[i] == 0) {
            resId = resources.getIdentifier("back_0", "drawable", "hk.hku.cs.card24")
            cards[i].setImageResource(resId)
            cards[i].isClickable = false
            value = data[i]
            num = value.toString()
            expression.append(num)
            imageCount[i]++
        }
    }
    private fun checkInput(input: String): Boolean {
        val jep = Jep()
        val res: Any = try {
            jep.parse(input)
            jep.evaluate()
        } catch (e: ParseException) {
            e.printStackTrace()
            Toast.makeText(
                this@MainActivity,
                "Wrong Expression", Toast.LENGTH_SHORT
            ).show()
            return false
        } catch (e: EvaluationException) {
            e.printStackTrace()
            Toast.makeText(
                this@MainActivity,
                "Wrong Expression", Toast.LENGTH_SHORT
            ).show()
            return false
        }
        val ca = res as Double
        return abs(ca - 24) < 1e-6
    }

}
