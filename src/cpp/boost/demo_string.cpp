#include <boost/algorithm/string.hpp>
#include <iostream>

int main() {
    std::string text = "Boost C++ Libraries - hello";
    boost::to_upper(text);
    std::cout << text << std::endl;
    return 0;
}
