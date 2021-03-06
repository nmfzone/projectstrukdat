
import java.util.Scanner;

class Kalkulator {

	private Queue rumus;
	private Queue splitRumus;
	private String postFix = "";
	private Stack rumusPostFix;
	private Stack hitung;
	private String originalRumus;
	private Stack tempOperand;
	private Stack tempOperator;
	private String[] operator = {"^", "%", "*", "/", "+", "-"};
	private int[] priority = {5, 4, 3, 2, 1, 0};
	private String[] variables = {"a", "b", "x", "y"};

	public Kalkulator()
	{
		rumus = new Queue();
		splitRumus = new Queue();
		rumusPostFix = new Stack();
		hitung = new Stack();
		tempOperand = new Stack();
		tempOperator = new Stack();
	}

	public double calculate()
	{
		double results=0, val1, val2, calc = 0;
		boolean isOperator;

		this.makePostFix();

		// System.out.println(postFix); //testCase

		String[] parts = postFix.split(",");

		for (int i = 0; i < parts.length;i++)
		{
			isOperator = false;
			String test = parts[i];

            for (int j = 0;j < 6;j++)
			{
				if (test.equals(operator[j]))
				{
					isOperator = true;
					break;
				}
			}

			if (!isOperator)
			{
				hitung.push(new Element(new Info(test)));
			}
			else
			{
				val1 = Double.parseDouble(hitung.pop().getInfo().getIsi());
				val2 = Double.parseDouble(hitung.pop().getInfo().getIsi());
				switch (test)
				{
					case "^":
						calc = Math.pow(val2, val1);
						// System.out.println("^ => " + val2 + "^" + val1 + " = " + calc); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
					case "%":
						calc = val2 % val1;
						// System.out.println("% => " + val2 + " % " + val1); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
					case "*":
						calc = val2 * val1;
						// System.out.println("* => " + val2 + " * " + val1); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
					case "/":
						calc = val2 / val1;
						// System.out.println("/ => " + val2 + " / " + val1); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
					case "+":
						calc = val2 + val1;
						// System.out.println("+ => " + val2 + " + " + val1); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
					case "-":
						calc = val2 - val1;
						// System.out.println("- => " + val2 + " - " + val1); //testCase
						hitung.push(new Element(new Info(String.valueOf(calc))));
						break;
				}
			}

	    }

	    results = Double.parseDouble(hitung.pop().getInfo().getIsi());

	    return results;
	}

	public void makePostFix()
	{
		boolean isOperator;
		int opNow, opPeek;

		this.makeRumusSparated();

		// splitRumus.print(); //testCase
	
		while (!splitRumus.isEmpty())
		{
			isOperator = false;
			String pop = splitRumus.deQueue().getInfo().getIsi();

			// Make from Infix to Postfix
			for (int i = 0; i < 6; i++)
			{
				if (pop.equals(operator[i]))
				{
					if (tempOperator.isEmpty())
					{
						tempOperator.push(new Element(new Info(pop)));
					}
					else
					{
						opNow = priority[i];
						opPeek = 0;

						for (int j = 0;j < 6;j++)
						{
							if (tempOperator.peek().getInfo().getIsi().equals(operator[j]))
							{
								opPeek = priority[j];
								break;
							}
						}

						if (opNow > opPeek)
						{
							tempOperator.push(new Element(new Info(pop)));
						}
						else
						{
							while (opNow <= opPeek)
							{
								if (!tempOperator.isEmpty())
								{
									tempOperand.push(tempOperator.pop());

									if (!tempOperator.isEmpty())
									{
										for (int j = 0;j < 6;j++)
										{
											if (tempOperator.peek().getInfo().getIsi().equals(operator[j]))
											{
												opPeek = priority[j];
												break;
											}
										}
									}
								}
								else
								{
									break;
								}
							}
							tempOperator.push(new Element(new Info(pop)));
						}
					}
					isOperator = true;
				}
			}
			
			if(!isOperator)
			{
				tempOperand.push(new Element(new Info(pop)));
			}
		}

		// Concatenate the Operand and Operator to rumusPostFix
		rumusPostFix = tempOperand;
		// System.out.println("Operator : "); //testCase
		// tempOperator.print(); //testCase
		while (!tempOperator.isEmpty())
		{
			rumusPostFix.push(tempOperator.pop());
		}

		Stack reversePostFix = new Stack();

		while (!rumusPostFix.isEmpty())
		{
			reversePostFix.push(new Element(new Info(rumusPostFix.pop().getInfo().getIsi())));
		}

		// Reverse the reversePostFix Stack to String
		// ex. "cba" to "abc" and add separate with comma (,)
		while (!reversePostFix.isEmpty())
		{
			postFix += reversePostFix.pop().getInfo().getIsi() + ",";
		}
		
		// Replace Variable with value if contains Variable
		this.replaceVariable();

	}

	public void addRumus(String rumus, int index)
	{
		this.rumus.enQueue(new Element(new Info(rumus, index)));
	}

	public void removeRumus(int index)
	{
		rumus.removeAt(index);
	}

	public Queue getRumus()
	{
		return rumus;
	}

	public String getOriginalRumus()
	{
		return originalRumus;
	}

	public void makeRumusSparated()
	{
		Element helper = rumus.deQueue();
		originalRumus = helper.getInfo().getIsi();
		String isi, value = "";
		boolean isOperator, isVar;
		
		for (int i = 0;i <= helper.getInfo().getIsi().length();i++)
		{
			if (i != helper.getInfo().getIsi().length())
			{
				isi = String.valueOf(helper.getInfo().getIsi().charAt(i));
				isOperator = isVar = false;

				for (int j = 0;j < operator.length;j++)
				{
					if (isi.toLowerCase().equals(operator[j]))
					{
						isOperator = true;
						break;
					}
				}

				for (int j = 0;j < variables.length;j++)
				{
					if (isi.toLowerCase().equals(variables[j]))
					{
						isVar = true;
						break;
					}
				}

				if (!isOperator && !isVar)
				{
					value += isi;
				}
				else if (isOperator || isVar)
				{
					if (!value.equals(""))
					{
						splitRumus.enQueue(new Element(new Info(value)));
					}

					if (isVar)
					{
						this.addOperatorIfBeforeVarIsNumber(helper, isi, i);
					}

					splitRumus.enQueue(new Element(new Info(isi)));

					if (isVar)
					{
						this.addOperatorIfAfterVarIsNumber(helper, isi, i);
					}

					value = "";
				}
			}
			else
			{
				if (!value.equals(""))
				{
					splitRumus.enQueue(new Element(new Info(value)));
				}
			}
		}
	}

	public void addOperatorIfBeforeVarIsNumber(Element helper, String isi, int index)
	{
		String before = "";
		boolean beforeIsOp = false;
		for (int j = 0;j < variables.length;j++)
		{
			if (isi.toLowerCase().equals(variables[j]))
			{
				if (index > 0)
				{
					// Check if before variable is not operator
					// then add * before the variable
					before = String.valueOf(helper.getInfo().getIsi().charAt((index-1)));
					for (int k = 0;k < operator.length;k++)
					{
						if (before.equals(operator[k]))
						{
							beforeIsOp = true;
							break;
						}
					}

					if (!beforeIsOp)
					{
						splitRumus.enQueue(new Element(new Info("*")));
					}

					break;
				}
			}
		}
	}

	public void addOperatorIfAfterVarIsNumber(Element helper, String isi, int index)
	{
		String after;
		boolean afterIsOp = false;
		for (int j = 0;j < variables.length;j++)
		{
			if (isi.toLowerCase().equals(variables[j]))
			{
				if (index <= helper.getInfo().getIsi().length()-2)
				{
					// Check if after variable is not operator
					// then add * after the variable
					after = String.valueOf(helper.getInfo().getIsi().charAt((index+1)));
					for (int k = 0;k < operator.length;k++)
					{
						if (after.equals(operator[k]))
						{
							afterIsOp = true;
							break;
						}
					}

					if (!afterIsOp)
					{
						splitRumus.enQueue(new Element(new Info("*")));
					}

					break;
				}
			}
		}
	}

	public void replaceVariable()
	{
		Scanner io = new Scanner(System.in);
		for (int i = 0;i < postFix.length();i++)
		{
			String karakter = String.valueOf(postFix.charAt(i));
			for (int j = 0;j < variables.length;j++)
			{
				if (karakter.toLowerCase().equals(variables[j]))
				{
					String varIn;
					System.out.print("Masukkan Value " + variables[j] + " : ");
					varIn = io.next();

					postFix = postFix.replaceAll(karakter, varIn);
					break;
				}
			}
		}
	}

	public void printRumus()
	{
		if (!rumus.isEmpty())
		{
			System.out.println("Daftar Rumus : ");
			rumus.print();
		}
		else
		{
			System.out.println("Rumus masih Kosong!");
		}
	}

}