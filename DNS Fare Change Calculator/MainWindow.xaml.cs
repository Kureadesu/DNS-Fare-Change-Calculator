using System.Windows;

namespace DNS_Fare_Change_Calculator
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void BulakanGuiguinto_Click(object sender, RoutedEventArgs e)
        {
            var window = new BulakanGuiguintoWindow();
            window.Show();
            this.Close();
        }

        private void BulakanBalagtas_Click(object sender, RoutedEventArgs e)
        {
            var window = new BulakanBalagtasWindow();
            window.Show();
            this.Close();
        }

        private void BulakanMalolos_Click(object sender, RoutedEventArgs e)
        {
            var window = new BulakanMalolosWindow();
            window.Show();
            this.Close();
        }

        private void Exit_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }
    }
}