import matplotlib.pyplot as plt

# Example data for 5 lines
x_values = [25000, 30000, 35000, 40000, 45000]
Rank10Ss = [3575, 3604, 4100, 3950, 4714]  # Replace with your data
Rank50Ss = [3620, 3753, 4404, 4223, 4695]   # Replace with your data
Rank100Ss = [3427, 3759, 4488, 4440, 4975]  # Replace with your data
Rank150Ss = [3600, 3663, 4217, 4422, 5069]   # Replace with your data
Rank200Ss = [3610, 4027, 4175, 4326, 5091]  # Replace with your data

Rank10Os = [425, 564, 615, 741, 808]  # Replace with your data
Rank50Os = [447, 509, 569, 666, 819]   # Replace with your data
Rank100Os = [474, 500, 575, 678, 784]  # Replace with your data
Rank150Os = [425, 513, 556, 674, 781]   # Replace with your data
Rank200Os = [474, 503, 569, 675, 760]  # Replace with your data

Rank10Sc = [26619, 50627, 80998, 92919, 113839]  # Replace with your data
Rank50Sc = [27271, 54498, 83266, 102273, 117408]   # Replace with your data
Rank100Sc = [26759, 52824, 82259, 96209, 122315]  # Replace with your data
Rank150Sc = [25713, 52100, 84446, 100749, 121782]   # Replace with your data
Rank200Sc = [26932, 54319, 81331, 98296, 121311]  # Replace with your data

Rank10Oc = [1886, 5857, 8568, 10185, 16611]  # Replace with your data
Rank50Oc = [1917, 5663, 8564, 10837, 17030]   # Replace with your data
Rank100Oc = [1950, 5544, 8444, 10460, 17046]  # Replace with your data
Rank150Oc = [2048, 5117, 8537, 10917, 16408]   # Replace with your data
Rank200Oc = [1908, 5366, 8681, 11487, 16272]  # Replace with your data

# Create line graph
plt.figure(figsize=(10, 6))
plt.plot(x_values, Rank10Os, marker='o', label='10 ranks simple')
plt.plot(x_values, Rank50Os, marker='o', label='50 ranks simple')
plt.plot(x_values, Rank100Os, marker='o', label='100 ranks simple')
plt.plot(x_values, Rank150Os, marker='o', label='150 ranks simple')
plt.plot(x_values, Rank200Os, marker='o', label='200 ranks simple')

plt.plot(x_values, Rank10Oc, marker='o', label='10 ranks complex')
plt.plot(x_values, Rank50Oc, marker='o', label='50 ranks complex')
plt.plot(x_values, Rank100Oc, marker='o', label='100 ranks complex')
plt.plot(x_values, Rank150Oc, marker='o', label='150 ranks complex')
plt.plot(x_values, Rank200Oc, marker='o', label='200 ranks complex')

plt.xlabel('Defeasible Implications')
plt.ylabel('Execution Time')
plt.title('Simple & Complex DIs - Optimised Generator')
plt.legend()
plt.grid(True)
plt.savefig('line_graph3.png')
plt.show()
